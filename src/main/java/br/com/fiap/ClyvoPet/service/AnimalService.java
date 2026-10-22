package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.model.Animal;
import br.com.fiap.ClyvoPet.repository.AnimalRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalService {

    private final AnimalRepository repository;

    public AnimalService(AnimalRepository repository) {
        this.repository = repository;
    }

    public void salvar(Animal animal) {
        repository.salvar(animal);
    }

    public List<Animal> listarTodos() {
        return repository.listarTodos();
    }

    public Optional<Animal> buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public List<Animal> buscarPorEspecie(String especie) {
        return repository.buscarPorEspecie(especie);
    }

    public void atualizar(Animal animal) {

        buscarPorId(animal.getId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Animal não encontrado com ID: "
                                        + animal.getId()
                        )
                );

        repository.atualizar(animal);
    }

    public void deletar(Long id) {

        buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Animal não encontrado com ID: " + id
                        )
                );

        repository.deletar(id);
    }
}