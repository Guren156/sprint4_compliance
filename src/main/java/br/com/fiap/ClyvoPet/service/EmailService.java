package br.com.fiap.ClyvoPet.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarEmail(
            String destinatario,
            String assunto,
            String mensagem) {

        try {

            SimpleMailMessage email = new SimpleMailMessage();

            email.setFrom(remetente);
            email.setTo(destinatario);
            email.setSubject(assunto);
            email.setText(mensagem);

            mailSender.send(email);

            System.out.println(
                    "✅ E-mail enviado com sucesso para: " + destinatario
            );

        } catch (Exception e) {

            System.err.println(
                    "❌ Erro ao enviar e-mail para: " + destinatario
            );

            e.printStackTrace();

            throw new RuntimeException(
                    "Erro ao enviar e-mail: " + e.getMessage(),
                    e
            );
        }
    }
}