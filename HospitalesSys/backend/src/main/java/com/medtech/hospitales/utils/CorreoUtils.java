package com.medtech.hospitales.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.io.IOException;


/**
 * Clase utilitaria para enviar correos electrónicos usando SMTP de Gmail.
 */
public class CorreoUtils {

    /**
     * Dirección de correo remitente.
     */
    private static final String REMITENTE = "kds2games@gmail.com";

    /**
     * Contraseña de la cuenta de correo (clave de aplicación).
     */
    private static final String CLAVE = "ncsa noau lkcd gics";

    /**
     * Inicializa la sesión de correo configurada para Gmail con autenticación y TLS.
     *
     * @return Sesión de correo lista para enviar mensajes.
     */
    private static Session iniciarSesionCorreo() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CLAVE);
            }
        });
    }

    /**
     * Envía un correo electrónico a un destinatario específico.
     *
     * @param destinatario Dirección de correo del destinatario.
     * @param asunto Asunto del correo.
     * @param mensaje Cuerpo del mensaje a enviar.
     * @throws MessagingException Si ocurre un error durante el envío.
     */
    public static void enviarCorreo(String destinatario, String asunto, String mensaje) throws MessagingException {
        Session session = iniciarSesionCorreo();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(REMITENTE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(asunto);
        message.setText(mensaje);

        Transport.send(message);
    }

    private static byte[] leerBytes(InputStream input) {
    try {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[4096];
        while ((nRead = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        buffer.flush();
        return buffer.toByteArray();
    } catch (IOException e) {
        throw new RuntimeException("Error al leer el archivo adjunto", e);
    }
}

    public static void enviarCorreoConAdjunto(String destinatario, String asunto, String mensaje, InputStream adjunto, String nombreArchivo) throws MessagingException {
    Session session = iniciarSesionCorreo();

    MimeBodyPart texto = new MimeBodyPart();
    texto.setText(mensaje);

    MimeBodyPart archivo = new MimeBodyPart();
    archivo.setFileName(nombreArchivo);
    archivo.setContent(leerBytes(adjunto), "application/pdf");

    Multipart multipart = new MimeMultipart();
    multipart.addBodyPart(texto);
    multipart.addBodyPart(archivo);

    Message email = new MimeMessage(session);
    email.setFrom(new InternetAddress(REMITENTE));
    email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
    email.setSubject(asunto);
    email.setContent(multipart);

    Transport.send(email);
}

    /**
 * Envía un correo de bienvenida al paciente recién activado en el sistema hospitalario.
 * Incluye su contraseña generada y las instrucciones para acceder.
 *
 * @param destinatario Correo del paciente
 * @param nombre Nombre completo del paciente
 * @param contrasena Contraseña generada para el sistema hospitalario
 * @throws MessagingException Si ocurre un error durante el envío
 */
public static void enviarCorreoBienvenidaPacienteConContrasena(String destinatario, String nombre, String contrasena) throws MessagingException {
    String asunto = "Bienvenido al sistema de hospitales MEDTECH";
    String mensaje = """
        ¡Hola %s!

        Tu cuenta ha sido activada exitosamente en el sistema de hospitales MEDTECH.

        Ahora puedes ingresar con las siguientes credenciales:
        Usuario: %s
        Contraseña: %s

        Recomendamos cambiar tu contraseña después de iniciar sesión.

        Saludos,
        Equipo MEDTECH
        """.formatted(nombre, destinatario, contrasena);

    enviarCorreo(destinatario, asunto, mensaje);
}


}
