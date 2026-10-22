package br.com.fiap.ClyvoPet.config;

import br.com.fiap.ClyvoPet.repository.VeterinarioRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final VeterinarioRepository repository;

    public CustomUserDetailsService(VeterinarioRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return repository.buscarPorEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Veterinário não encontrado: " + email
                        )
                );
    }
}