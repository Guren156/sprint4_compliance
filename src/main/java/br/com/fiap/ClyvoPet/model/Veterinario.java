package br.com.fiap.ClyvoPet.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class Veterinario implements UserDetails {

    private Long id;

    @NotBlank(message = "O nome do veterinário é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    private String email;

    /*
     * A senha é obrigatória no cadastro, mas não na edição.
     * Por isso, a obrigatoriedade continua sendo tratada
     * no fluxo de cadastro/service.
     */
    @Size(max = 255, message = "A senha deve ter no máximo 255 caracteres.")
    private String senha;

    @NotBlank(message = "O CRMV é obrigatório.")
    @Size(max = 20, message = "O CRMV deve ter no máximo 20 caracteres.")
    private String crmv;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    private String telefone;

    @Size(max = 50, message = "A especialidade deve ter no máximo 50 caracteres.")
    private String especialidade;

    private LocalDateTime dataCadastro;

    private Boolean ativo;

    @Size(max = 20, message = "O perfil deve ter no máximo 20 caracteres.")
    private String role;

    // ===== CONSTRUTORES =====

    public Veterinario() {
    }

    public Veterinario(
            String nome,
            String email,
            String senha,
            String crmv,
            String telefone,
            String especialidade,
            String role) {

        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.crmv = crmv;
        this.telefone = telefone;
        this.especialidade = especialidade;
        this.role = role;
        this.ativo = true;
        this.dataCadastro = LocalDateTime.now();
    }

    // ===== MÉTODOS DO UserDetails =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
                new SimpleGrantedAuthority("ROLE_" + this.role)
        );
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return ativo;
    }

    // ===== GETTERS E SETTERS =====

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCrmv() {
        return crmv;
    }

    public void setCrmv(String crmv) {
        this.crmv = crmv;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}