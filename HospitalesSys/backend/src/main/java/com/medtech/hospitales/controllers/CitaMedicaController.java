package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.CitaRegistroDTO;
import com.medtech.hospitales.dtos.CitaDTO;
import com.medtech.hospitales.services.CitaMedicaService;
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
            System.out.println("🟢 DTO parseado correctamente: " + dto.getFechaHora());

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
            Long idUsuario = ctx.attribute("idUsuario");
    
            List<CitaDTO> citas = service.obtenerCitasPorDoctor(idUsuario);
            ctx.json(citas);
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).json(Map.of("error", "Error al obtener citas del doctor"));
        }
    }   
    
}
