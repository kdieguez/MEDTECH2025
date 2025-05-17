package com.medtech.hospitales.controllers;

import com.medtech.hospitales.utils.CorreoUtils;
import io.javalin.http.Context;
import io.javalin.http.UploadedFile;
import jakarta.mail.MessagingException;

import java.io.InputStream;
import java.util.Map;

public class RecetaCorreoController {

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

            CorreoUtils.enviarCorreoConAdjunto(correo, "Receta Médica - " + extraerNombreDesdeMensaje(mensaje), mensaje, input, nombreArchivo);
            ctx.status(200).json(Map.of("mensaje", "Correo enviado exitosamente"));
        } catch (MessagingException e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al enviar correo: " + e.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno al procesar la solicitud"));
        }
    }

    private String extraerNombreDesdeMensaje(String mensaje) {
        String[] palabras = mensaje.split(" ");
        for (int i = 0; i < palabras.length - 1; i++) {
            if (palabras[i].equalsIgnoreCase("paciente") || palabras[i].equalsIgnoreCase("Paciente")) {
                return palabras[i + 1];
            }
        }
        return "Paciente";
    }
}
