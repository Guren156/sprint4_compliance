package br.com.fiap.ClyvoPet.model;

import java.time.LocalDateTime;

public class Lembrete {

    private Long id;
    private Long consultaId;
    private String tutorEmail;
    private String tutorTelefone;
    private String mensagem;
    private LocalDateTime dataEnvio;
    private Boolean enviado;
    private String tipo;

    // Campos auxiliares para exibição
    private String animalNome;
    private String tutorNome;

    public Lembrete() {
    }

    public Lembrete(
            Long consultaId,
            String tutorEmail,
            String mensagem,
            String tipo) {

        this.consultaId = consultaId;
        this.tutorEmail = tutorEmail;
        this.mensagem = mensagem;
        this.tipo = tipo;
        this.enviado = false;
        this.dataEnvio = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public void setTutorEmail(String tutorEmail) {
        this.tutorEmail = tutorEmail;
    }

    public String getTutorTelefone() {
        return tutorTelefone;
    }

    public void setTutorTelefone(String tutorTelefone) {
        this.tutorTelefone = tutorTelefone;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Boolean getEnviado() {
        return enviado;
    }

    public void setEnviado(Boolean enviado) {
        this.enviado = enviado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // =========================
    // CAMPOS AUXILIARES
    // =========================

    public String getAnimalNome() {
        return animalNome;
    }

    public void setAnimalNome(String animalNome) {
        this.animalNome = animalNome;
    }

    public String getTutorNome() {
        return tutorNome;
    }

    public void setTutorNome(String tutorNome) {
        this.tutorNome = tutorNome;
    }
}