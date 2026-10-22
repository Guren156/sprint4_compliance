package br.com.fiap.ClyvoPet.service;

import br.com.fiap.ClyvoPet.model.Animal;
import br.com.fiap.ClyvoPet.model.Consulta;
import br.com.fiap.ClyvoPet.model.Lembrete;
import br.com.fiap.ClyvoPet.repository.AnimalRepository;
import br.com.fiap.ClyvoPet.repository.ConsultaRepository;
import br.com.fiap.ClyvoPet.repository.LembreteRepository;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class LembreteService {

    private final LembreteRepository lembreteRepository;
    private final ConsultaRepository consultaRepository;
    private final AnimalRepository animalRepository;
    private final EmailService emailService;

    public LembreteService(
            LembreteRepository lembreteRepository,
            ConsultaRepository consultaRepository,
            AnimalRepository animalRepository,
            EmailService emailService) {

        this.lembreteRepository = lembreteRepository;
        this.consultaRepository = consultaRepository;
        this.animalRepository = animalRepository;
        this.emailService = emailService;
    }

    // =========================
    // SALVAR
    // =========================
    public void salvar(Lembrete lembrete) {

        if (lembrete.getConsultaId() == null) {
            throw new RuntimeException(
                    "O ID da consulta é obrigatório."
            );
        }

        if (lembrete.getEnviado() == null) {
            lembrete.setEnviado(false);
        }

        if (lembrete.getTipo() == null ||
                lembrete.getTipo().isBlank()) {

            lembrete.setTipo("EMAIL");
        }

        lembreteRepository.salvar(lembrete);
    }

    // =========================
    // LISTAR TODOS
    // =========================
    public List<Lembrete> listarTodos() {
        return lembreteRepository.listarTodos();
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    public Optional<Lembrete> buscarPorId(Long id) {
        return lembreteRepository.buscarPorId(id);
    }

    // =========================
    // BUSCAR PENDENTES
    // =========================
    public List<Lembrete> buscarPendentes() {
        return lembreteRepository.buscarPendentes();
    }

    // =========================
    // BUSCAR POR CONSULTA
    // =========================
    public List<Lembrete> buscarPorConsulta(Long consultaId) {
        return lembreteRepository.buscarPorConsulta(consultaId);
    }

    // =========================
    // CRIAR LEMBRETE
    // =========================
    public Lembrete criarLembrete(Long consultaId) {

        Consulta consulta = consultaRepository.buscarPorId(consultaId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Consulta não encontrada com ID: "
                                        + consultaId
                        )
                );

        Animal animal = animalRepository.buscarPorId(
                        consulta.getAnimalId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Animal não encontrado com ID: "
                                        + consulta.getAnimalId()
                        )
                );

        if (animal.getTutorEmail() == null ||
                animal.getTutorEmail().isBlank()) {

            throw new RuntimeException(
                    "O tutor do animal "
                            + animal.getNome()
                            + " não possui e-mail cadastrado."
            );
        }

        String dataConsulta = "Data não informada";

        if (consulta.getDataHora() != null) {

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern(
                            "dd/MM/yyyy 'às' HH:mm"
                    );

            dataConsulta =
                    consulta.getDataHora().format(formatter);
        }

        String veterinario =
                consulta.getVeterinarioNome() != null
                        ? consulta.getVeterinarioNome()
                        : "Não informado";

        String motivo =
                consulta.getMotivo() != null &&
                        !consulta.getMotivo().isBlank()
                        ? consulta.getMotivo()
                        : "Não informado";

        String mensagem =
                "Olá, " + animal.getTutorNome() + "!\n\n"
                        + "Este é um lembrete da ClyvoPet sobre a consulta "
                        + "do seu animal " + animal.getNome() + ".\n\n"

                        + "Data da consulta: "
                        + dataConsulta + "\n"

                        + "Animal: "
                        + animal.getNome() + "\n"

                        + "Veterinário: "
                        + veterinario + "\n"

                        + "Motivo: "
                        + motivo + "\n\n"

                        + "Caso não possa comparecer, entre em contato "
                        + "com a clínica.\n\n"

                        + "Atenciosamente,\n"
                        + "Equipe ClyvoPet 🐾";

        Lembrete lembrete = new Lembrete();

        lembrete.setConsultaId(
                consulta.getId()
        );

        lembrete.setTutorEmail(
                animal.getTutorEmail()
        );

        lembrete.setTutorTelefone(
                animal.getTutorTelefone()
        );

        lembrete.setMensagem(
                mensagem
        );

        lembrete.setTipo(
                "EMAIL"
        );

        lembrete.setEnviado(
                false
        );

        lembreteRepository.salvar(lembrete);

        System.out.println(
                "📝 Lembrete criado com ID: "
                        + lembrete.getId()
        );

        return lembrete;
    }

    // =========================
    // ENVIAR LEMBRETE
    // =========================
    public void enviarLembrete(Long lembreteId) {

        Lembrete lembrete =
                lembreteRepository.buscarPorId(lembreteId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Lembrete não encontrado com ID: "
                                                + lembreteId
                                )
                        );

        if (Boolean.TRUE.equals(
                lembrete.getEnviado())) {

            throw new RuntimeException(
                    "Este lembrete já foi enviado."
            );
        }

        if (lembrete.getTutorEmail() == null ||
                lembrete.getTutorEmail().isBlank()) {

            throw new RuntimeException(
                    "O lembrete não possui e-mail de destinatário."
            );
        }

        System.out.println(
                "📧 Tentando enviar e-mail para: "
                        + lembrete.getTutorEmail()
        );

        emailService.enviarEmail(
                lembrete.getTutorEmail(),
                "Lembrete de Consulta - ClyvoPet 🐾",
                lembrete.getMensagem()
        );

        lembreteRepository.marcarComoEnviado(
                lembrete.getId()
        );

        System.out.println(
                "✅ Lembrete enviado com sucesso. ID: "
                        + lembrete.getId()
        );
    }

    // =========================
    // CRIAR E ENVIAR
    // =========================
    public void criarEEnviarLembrete(
            Long consultaId) {

        System.out.println(
                "🔔 Criando lembrete para consulta ID: "
                        + consultaId
        );

        Lembrete lembrete =
                criarLembrete(consultaId);

        if (lembrete.getId() == null) {

            throw new IllegalStateException(
                    "O lembrete foi criado, "
                            + "mas o ID não foi recuperado."
            );
        }

        enviarLembrete(
                lembrete.getId()
        );
    }

    // =========================
    // ENVIAR TODOS PENDENTES
    // =========================
    public void enviarTodosLembretesPendentes() {

        List<Lembrete> pendentes =
                lembreteRepository.buscarPendentes();

        System.out.println(
                "🔔 Lembretes pendentes encontrados: "
                        + pendentes.size()
        );

        for (Lembrete lembrete : pendentes) {

            try {

                enviarLembrete(
                        lembrete.getId()
                );

            } catch (Exception e) {

                System.err.println(
                        "❌ Erro ao enviar lembrete ID "
                                + lembrete.getId()
                                + ": "
                                + e.getMessage()
                );

                e.printStackTrace();
            }
        }
    }

    // =========================
    // MARCAR COMO ENVIADO
    // =========================
    public void marcarComoEnviado(Long id) {

        lembreteRepository.buscarPorId(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Lembrete não encontrado com ID: "
                                        + id
                        )
                );

        lembreteRepository.marcarComoEnviado(id);
    }
}