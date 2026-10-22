package br.com.fiap.ClyvoPet.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class Animal {

    private Long id;

    @NotBlank(message = "O nome do animal é obrigatório.")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres.")
    private String nome;

    @NotBlank(message = "A espécie é obrigatória.")
    @Size(max = 50, message = "A espécie deve ter no máximo 50 caracteres.")
    private String especie;

    @Size(max = 50, message = "A raça deve ter no máximo 50 caracteres.")
    private String raca;

    @PositiveOrZero(message = "A idade não pode ser negativa.")
    private Integer idade;

    @PositiveOrZero(message = "O peso não pode ser negativo.")
    private Double peso;

    @Size(max = 1, message = "O sexo deve conter apenas 1 caractere.")
    private String sexo;

    @NotBlank(message = "O nome do tutor é obrigatório.")
    @Size(max = 100, message = "O nome do tutor deve ter no máximo 100 caracteres.")
    private String tutorNome;

    @NotBlank(message = "O telefone do tutor é obrigatório.")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres.")
    private String tutorTelefone;

    @Email(message = "Informe um e-mail válido.")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres.")
    private String tutorEmail;

    private LocalDateTime dataCadastro;

    @Size(max = 500, message = "As observações devem ter no máximo 500 caracteres.")
    private String observacoes;

    private Boolean ativo;

    // Construtores
    public Animal() {
    }

    public Animal(String nome, String especie, String tutorNome, String tutorTelefone) {
        this.nome = nome;
        this.especie = especie;
        this.tutorNome = tutorNome;
        this.tutorTelefone = tutorTelefone;
        this.ativo = true;
        this.dataCadastro = LocalDateTime.now();
    }

    // Getters e Setters
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

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }

    public Integer getIdade() {
        return idade;
    }

    public void setIdade(Integer idade) {
        this.idade = idade;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getTutorNome() {
        return tutorNome;
    }

    public void setTutorNome(String tutorNome) {
        this.tutorNome = tutorNome;
    }

    public String getTutorTelefone() {
        return tutorTelefone;
    }

    public void setTutorTelefone(String tutorTelefone) {
        this.tutorTelefone = tutorTelefone;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", especie='" + especie + '\'' +
                ", tutor='" + tutorNome + '\'' +
                '}';
    }
}