package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.CitaMedica;
import com.medtech.hospitales.services.CitasService;
import io.javalin.Javalin;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CitasController {

    private static final CitasService citasService = new CitasService();

    public static void addRoutes(Javalin app) {

        app.get("/api/citas", ctx -> {
            List<CitaMedica> citas = citasService.listarTodas();
            ctx.json(citas);
        });

        app.get("/api/citas/disponibilidad", ctx -> {
            String fechaStr = ctx.queryParam("fecha");
            Long doctorId = Long.parseLong(ctx.queryParam("doctorId"));

            LocalDate fecha = LocalDate.parse(fechaStr, DateTimeFormatter.ISO_DATE);
            List<String> horarios = citasService.obtenerHorariosDisponibles(fecha, doctorId);

            ctx.json(horarios);
        });

        app.post("/api/citas", ctx -> {
            CitaMedica cita = ctx.bodyAsClass(CitaMedica.class);
            citasService.crearCita(cita);
            ctx.status(201);
        });
    }
}
