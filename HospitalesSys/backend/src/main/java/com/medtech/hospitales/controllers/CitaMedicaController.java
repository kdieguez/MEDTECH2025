package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.CitaRegistroDTO;
import com.medtech.hospitales.dtos.FormularioCitaDTO;
import com.medtech.hospitales.dtos.CitaDTO;
import com.medtech.hospitales.services.CitaMedicaService;
import com.medtech.hospitales.services.FormularioCitaService;
import com.medtech.hospitales.services.RecetaMedicaService;

import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class CitaMedicaController {

    private final CitaMedicaService service = new CitaMedicaService();
    private final ObjectMapper objectMapper;

    public CitaMedicaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void registrarCita(Context ctx) {
        try {
            CitaRegistroDTO dto = objectMapper.readValue(ctx.body(), CitaRegistroDTO.class);
            System.out.println("DTO parseado correctamente: " + dto.getFechaHora());

            service.registrarCita(dto);
            ctx.status(201).json("Cita registrada correctamente");

        } catch (RuntimeException ex) {
            ctx.status(400).json(Map.of("error", ex.getMessage()));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error interno al registrar la cita"));
        }
    }

    public void obtenerHorasDisponibles(Context ctx) {
        try {
            Long idDoctor = Long.parseLong(ctx.queryParam("idDoctor"));
            LocalDate fecha = LocalDate.parse(ctx.queryParam("fecha"));
    
            List<String> horas = service.obtenerHorasDisponiblesParaDoctor(idDoctor, fecha);
            ctx.json(horas);
    
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(400).json(Map.of("error", "Parámetros inválidos o error al obtener horarios"));
        }
    }

    public void obtenerTodasLasCitas(Context ctx) {
        try {   
            List<CitaDTO> citas = service.obtenerTodasLasCitas();
            ctx.json(citas);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener citas"));
        }
    }

    public void obtenerMisCitas(Context ctx) {
        try {
            Long idUsuario = Long.parseLong(ctx.queryParam("idUsuario")); // <-- ya corregido
            System.out.println("ID de usuario recibido en citas/mias: " + idUsuario);

            List<CitaDTO> citas = service.obtenerCitasPorDoctor(idUsuario);
            ctx.json(citas);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener citas del doctor"));
        }
    }

    public void guardarFormularioCita(Context ctx) {
        try {
            FormularioCitaDTO dto = objectMapper.readValue(ctx.body(), FormularioCitaDTO.class);

            FormularioCitaService service = new FormularioCitaService();
            service.guardarFormulario(dto);

            ctx.status(201).json(Map.of("mensaje", "Formulario de cita guardado exitosamente"));
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al guardar el formulario de cita"));
        }
    }

    public void obtenerDatosReceta(Context ctx) {
        try {
            Long idCita = Long.parseLong(ctx.pathParam("idCita"));
            FormularioCitaService service = new FormularioCitaService();
            var recetaDTO = service.generarReceta(idCita);
    
            ctx.json(recetaDTO);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al generar receta médica"));
        }
    }

    public void crearRecetaMedica(Context ctx) {
    try {
        Long idCita = Long.parseLong(ctx.pathParam("id"));
        RecetaMedicaService recetaService = new RecetaMedicaService();
        recetaService.crearEncabezadoReceta(idCita);

        ctx.status(201).json(Map.of("mensaje", "Receta médica creada exitosamente"));
    } catch (Exception e) {
        e.printStackTrace();
        ctx.status(500).json(Map.of("error", "Error al crear receta médica"));
    }
}

    
}
