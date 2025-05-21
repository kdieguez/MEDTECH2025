package com.medtech.hospitales.controllers;

import com.medtech.hospitales.utils.CorreoUtils;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import jakarta.mail.MessagingException;

import java.io.InputStream;
import java.util.Map;

/**
 * Controlador encargado de enviar recetas médicas en formato PDF por correo electrónico.
 * Recibe los datos por formulario y utiliza {@link CorreoUtils} para el envío del correo con adjunto.
 */
public class RecetaCorreoController {

    /**
     * Procesa una solicitud POST para enviar una receta médica por correo electrónico.
     * <p>
     * Requiere los siguientes campos:
     * <ul>
     *     <li><strong>correo</strong>: dirección de email del destinatario</li>
     *     <li><strong>mensaje</strong>: cuerpo del mensaje</li>
     *     <li><strong>pdf</strong>: archivo PDF a adjuntar</li>
     * </ul>
     * </p>
     *
     * @param ctx Contexto HTTP de Javalin con los parámetros del formulario.
     */
    public void enviarCorreoConPdf(Context ctx) {
        try {
            String correo = ctx.formParam("correo");
            String mensaje = ctx.formParam("mensaje");

            if (correo == null || mensaje == null) {
                ctx.status(400).json(Map.of("error", "Faltan campos obligatorios"));
                return;
            }

            UploadedFile archivo = ctx.uploadedFile("pdf");
            if (archivo == null) {
                ctx.status(400).json(Map.of("error", "Archivo PDF no recibido"));
                return;
            }

            InputStream input = archivo.content();
            String nombreArchivo = archivo.filename();

            CorreoUtils.enviarCorreoConAdjunto(
                correo,
                "Receta Médica - " + extraerNombreDesdeMensaje(mensaje),
                mensaje,
                input,
                nombreArchivo
            );

            ctx.status(200).json(Map.of("mensaje", "Correo enviado exitosamente"));
        } catch (MessagingException e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al enviar correo: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno al procesar la solicitud"));
        }
    }

    /**
     * Extrae el nombre del paciente desde el mensaje para personalizar el asunto del correo.
     *
     * @param mensaje mensaje recibido desde el formulario
     * @return nombre del paciente si se encuentra, o "Paciente" como valor por defecto
     */
    private String extraerNombreDesdeMensaje(String mensaje) {
        String[] palabras = mensaje.split(" ");
        for (int i = 0; i < palabras.length - 1; i++) {
            if (palabras[i].equalsIgnoreCase("paciente")) {
                return palabras[i + 1];
            }
        }
        return "Paciente";
    }
}
