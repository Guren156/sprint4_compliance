package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.model.Veterinario;
import br.com.fiap.ClyvoPet.repository.ConsultaRepository;
import br.com.fiap.ClyvoPet.repository.VeterinarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarioService {

    private final VeterinarioRepository veterinarioRepository;
    private final ConsultaRepository consultaRepository;
    private final PasswordEncoder passwordEncoder;

    public VeterinarioService(
            VeterinarioRepository veterinarioRepository,
            ConsultaRepository consultaRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.veterinarioRepository = veterinarioRepository;
        this.consultaRepository = consultaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void salvar(Veterinario veterinario) {

        if (veterinario.getSenha() == null
                || veterinario.getSenha().isBlank()) {

            throw new IllegalArgumentException(
                    "A senha do veterinário é obrigatória."
            );
        }

        veterinario.setSenha(
                passwordEncoder.encode(veterinario.getSenha())
        );

        if (veterinario.getRole() == null
                || veterinario.getRole().isBlank()) {

            veterinario.setRole("VETERINARIO");
        }

        veterinarioRepository.salvar(veterinario);
    }

    public Optional<Veterinario> buscarPorEmail(String email) {
        return veterinarioRepository.buscarPorEmail(email);
    }

    public Optional<Veterinario> buscarPorId(Long id) {
        return veterinarioRepository.buscarPorId(id);
    }

    public List<Veterinario> listarTodos() {
        return veterinarioRepository.listarTodos();
    }

    public void atualizar(Veterinario veterinario) {

        veterinarioRepository.buscarPorId(veterinario.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Veterinário não encontrado."
                        )
                );

        veterinarioRepository.atualizar(veterinario);
    }

    @Transactional
    public int deletar(Long id) {

        veterinarioRepository.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Veterinário não encontrado."
                        )
                );

        int consultasCanceladas =
                consultaRepository.cancelarConsultasPorVeterinario(id);

        veterinarioRepository.deletar(id);

        return consultasCanceladas;
    }

    public boolean autenticar(String email, String senha) {

        Optional<Veterinario> veterinarioOptional =
                veterinarioRepository.buscarPorEmail(email);

        if (veterinarioOptional.isEmpty()) {
            return false;
        }

        Veterinario veterinario =
                veterinarioOptional.get();

        if (!Boolean.TRUE.equals(veterinario.getAtivo())) {
            return false;
        }

        return passwordEncoder.matches(
                senha,
                veterinario.getSenha()
        );
    }
}