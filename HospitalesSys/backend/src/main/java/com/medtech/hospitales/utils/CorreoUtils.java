package com.medtech.hospitales.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.*;

public class CorreoUtils {

    private static final String REMITENTE = "kds2games@gmail.com";
    private static final String CLAVE = "ncsa noau lkcd gics";    

    private static Session iniciarSesionCorreo() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CLAVE);
            }
        });
    }

    public static void enviarCorreo(String destinatario, String asunto, String mensaje) throws MessagingException {
        Session session = iniciarSesionCorreo();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(REMITENTE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(asunto);
        message.setText(mensaje);

        Transport.send(message);
    }
}
