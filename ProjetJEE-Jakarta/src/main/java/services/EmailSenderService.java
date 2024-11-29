package services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class EmailSenderService {

    public static void sendEmail(String recipientEmail, String subject, String  htmlContent) {
        // Configuration des propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true"); // Nécessite une authentification
        props.put("mail.smtp.starttls.enable", "true"); // Active STARTTLS
        props.put("mail.smtp.host", "smtp.gmail.com"); // Serveur SMTP
        props.put("mail.smtp.port", "587"); // Port SMTP sécurisé


        // Informations d'authentification
        String username = "cyscolarite2024@gmail.com";
        String password = "clkg tamv xqpv rtyp";

        // Création de la session avec authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username)); // Adresse de l'expéditeur
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail)); // Adresse du destinataire
            message.setSubject(subject); // Sujet de l'email
            message.setContent(htmlContent, "text/html");

            // Envoi du message
            Transport.send(message);

            System.out.println("Email envoyé avec succès à : " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
            e.printStackTrace();
        }
    }

}
