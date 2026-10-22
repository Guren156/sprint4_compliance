package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.model.Consulta;
import br.com.fiap.ClyvoPet.repository.AnimalRepository;
import br.com.fiap.ClyvoPet.repository.ConsultaRepository;
import br.com.fiap.ClyvoPet.repository.VeterinarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository repository;
    private final AnimalRepository animalRepository;
    private final VeterinarioRepository veterinarioRepository;

    public ConsultaService(
            ConsultaRepository repository,
            AnimalRepository animalRepository,
            VeterinarioRepository veterinarioRepository) {

        this.repository = repository;
        this.animalRepository = animalRepository;
        this.veterinarioRepository = veterinarioRepository;
    }

    public void salvar(Consulta consulta) {

        // Valida se o animal existe
        if (animalRepository
                .buscarPorId(consulta.getAnimalId())
                .isEmpty()) {

            throw new RuntimeException(
                    "Animal não encontrado com ID: "
                            + consulta.getAnimalId()
            );
        }

        // Valida se o veterinário existe
        if (veterinarioRepository
                .buscarPorId(consulta.getVeterinarioId())
                .isEmpty()) {

            throw new RuntimeException(
                    "Veterinário não encontrado com ID: "
                            + consulta.getVeterinarioId()
            );
        }

        repository.salvar(consulta);
    }

    public List<Consulta> listarTodas() {
        return repository.listarTodos();
    }

    public Optional<Consulta> buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<Consulta> buscarPorAnimal(Long animalId) {
        return repository.buscarPorAnimal(animalId);
    }

    public void atualizar(Consulta consulta) {

        buscarPorId(consulta.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: "
                                        + consulta.getId()
                        )
                );

        repository.atualizar(consulta);
    }

    public void deletar(Long id) {

        buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: " + id
                        )
                );

        repository.deletar(id);
    }

    public void cancelar(Long id) {

        Consulta consulta = buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: " + id
                        )
                );

        consulta.setStatus("CANCELADA");

        repository.atualizar(consulta);
    }

    public void finalizar(Long id) {

        Consulta consulta = buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: " + id
                        )
                );

        consulta.setStatus("REALIZADA");

        repository.atualizar(consulta);
    }

    public List<Consulta> buscarConsultasPendentesLembrete() {

        LocalDateTime agora = LocalDateTime.now();
        LocalDateTime limite = agora.plusDays(1);

        return repository.buscarConsultasPendentesLembrete(
                agora,
                limite
        );
    }

    public void marcarLembreteEnviado(Long id) {

        Consulta consulta = buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: " + id
                        )
                );

        consulta.setLembreteEnviado(true);

        repository.atualizar(consulta);
    }
}