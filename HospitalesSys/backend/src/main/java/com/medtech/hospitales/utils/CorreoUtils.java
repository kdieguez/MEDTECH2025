package com.medtech.hospitales.utils;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
import java.io.IOException;

/**
 * Clase utilitaria para enviar correos electrónicos usando SMTP de Gmail.
 * Permite enviar correos en texto plano, HTML y con archivos adjuntos.
 */
public class CorreoUtils {

    // Correo y clave para autenticación SMTP (usar con precaución, evitar hardcodear en producción)
    private static final String REMITENTE = "kds2games@gmail.com";
    private static final String CLAVE = "ncsa noau lkcd gics";

    /**
     * Inicializa la sesión SMTP con Gmail, configurando host, puerto y autenticación TLS.
     *
     * @return Sesión SMTP configurada para enviar correos.
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
     * Envía un correo en texto plano a un destinatario específico.
     *
     * @param destinatario Dirección de correo del destinatario.
     * @param asunto       Asunto del correo.
     * @param mensaje      Mensaje en texto plano.
     * @throws MessagingException si ocurre error en el envío.
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

    /**
     * Envía un correo con contenido HTML a un destinatario.
     *
     * @param destinatario Dirección de correo del destinatario.
     * @param asunto       Asunto del correo.
     * @param htmlMensaje  Mensaje en formato HTML.
     * @throws MessagingException si ocurre error en el envío.
     */
    public static void enviarCorreoHtml(String destinatario, String asunto, String htmlMensaje) throws MessagingException {
        Session session = iniciarSesionCorreo();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(REMITENTE));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(asunto);
        message.setContent(htmlMensaje, "text/html; charset=utf-8");

        Transport.send(message);
    }

    /**
     * Lee los bytes desde un InputStream y los devuelve en un arreglo de bytes.
     *
     * @param input InputStream del archivo adjunto.
     * @return Array de bytes con el contenido del archivo.
     * @throws RuntimeException si ocurre un error de lectura.
     */
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

    /**
     * Envía un correo con un archivo adjunto en formato PDF.
     *
     * @param destinatario Dirección de correo del destinatario.
     * @param asunto       Asunto del correo.
     * @param mensaje      Mensaje en texto plano.
     * @param adjunto      InputStream del archivo PDF adjunto.
     * @param nombreArchivo Nombre del archivo adjunto.
     * @throws MessagingException si ocurre error en el envío.
     */
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
     * Envía un correo de bienvenida a un paciente recién activado,
     * incluyendo la contraseña temporal para el primer ingreso.
     *
     * @param destinatario Dirección de correo del paciente.
     * @param nombre       Nombre completo del paciente.
     * @param contrasena   Contraseña temporal generada para el paciente.
     * @throws MessagingException si ocurre error en el envío.
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
