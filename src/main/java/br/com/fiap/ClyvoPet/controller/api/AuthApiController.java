package br.com.fiap.ClyvoPet.controller.api;

import br.com.fiap.ClyvoPet.config.JwtService;
import br.com.fiap.ClyvoPet.dto.auth.LoginRequest;
import br.com.fiap.ClyvoPet.dto.auth.LoginResponse;
import br.com.fiap.ClyvoPet.dto.auth.UserResponse;
import br.com.fiap.ClyvoPet.model.Veterinario;
import br.com.fiap.ClyvoPet.service.VeterinarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    private final AuthenticationManager authenticationManager;
    private final VeterinarioService veterinarioService;
    private final JwtService jwtService;

    public AuthApiController(
            AuthenticationManager authenticationManager,
            VeterinarioService veterinarioService,
            JwtService jwtService
    ) {
        this.authenticationManager =
                authenticationManager;

        this.veterinarioService =
                veterinarioService;

        this.jwtService =
                jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request
    ) {

        try {

            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(),
                                    request.getSenha()
                            )
                    );

            Veterinario veterinario =
                    (Veterinario)
                            authentication.getPrincipal();

            String token =
                    jwtService.gerarToken(veterinario);

            UserResponse user =
                    new UserResponse(
                            veterinario.getId(),
                            veterinario.getNome(),
                            veterinario.getEmail(),
                            veterinario.getRole()
                    );

            return ResponseEntity.ok(
                    new LoginResponse(
                            token,
                            user
                    )
            );

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            "E-mail ou senha inválidos."
                    );
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registrar(
            @RequestBody Veterinario veterinario
    ) {

        try {

            /*
             * Cadastro público nunca poderá
             * criar um ADMIN.
             */
            veterinario.setRole(
                    "VETERINARIO"
            );

            veterinarioService.salvar(
                    veterinario
            );

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            "Veterinário cadastrado com sucesso."
                    );

        } catch (Exception e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }
}