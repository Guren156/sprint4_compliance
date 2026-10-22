package br.com.fiap.ClyvoPet.model;

import java.time.LocalDateTime;

public class Consulta {

    private Long id;
    private Long animalId;
    private Long veterinarioId;

    private String animalNome;
    private String veterinarioNome;

    private LocalDateTime dataHora;
    private String motivo;
    private String diagnostico;
    private String prescricao;
    private String status;
    private Boolean lembreteEnviado;

    public Consulta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnimalId() {
        return animalId;
    }

    public void setAnimalId(Long animalId) {
        this.animalId = animalId;
    }

    public Long getVeterinarioId() {
        return veterinarioId;
    }

    public void setVeterinarioId(Long veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public String getAnimalNome() {
        return animalNome;
    }

    public void setAnimalNome(String animalNome) {
        this.animalNome = animalNome;
    }

    public String getVeterinarioNome() {
        return veterinarioNome;
    }

    public void setVeterinarioNome(String veterinarioNome) {
        this.veterinarioNome = veterinarioNome;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getPrescricao() {
        return prescricao;
    }

    public void setPrescricao(String prescricao) {
        this.prescricao = prescricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getLembreteEnviado() {
        return lembreteEnviado;
    }

    public void setLembreteEnviado(Boolean lembreteEnviado) {
        this.lembreteEnviado = lembreteEnviado;
    }
}