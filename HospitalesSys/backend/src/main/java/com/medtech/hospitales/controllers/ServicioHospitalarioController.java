package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.ServicioRegistroDTO;
import com.medtech.hospitales.models.ServicioHospitalario;
import com.medtech.hospitales.services.ServicioHospitalarioService;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;

import java.util.List;

public class ServicioHospitalarioController {

    private ServicioHospitalarioService servicioService = null;
    private final ObjectMapper mapper = new ObjectMapper();

    public ServicioHospitalarioController(EntityManager entityManager) {
        this.servicioService = new ServicioHospitalarioService(entityManager);
    }

    public Handler registrarServicio = ctx -> {
        try {
            ServicioRegistroDTO dto = mapper.readValue(ctx.body(), ServicioRegistroDTO.class);
            servicioService.registrarServicio(dto);
            ctx.status(201).result("Servicio hospitalario registrado exitosamente.");
        } catch (Exception e) {
            ctx.status(400).result("Error al registrar servicio: " + e.getMessage());
        }
    };

    public Handler listarServicios = ctx -> {
        List<ServicioHospitalario> servicios = servicioService.listarServicios();
        ctx.json(servicios);
    };

    public Handler detalleServicio = ctx -> {
        Long id = Long.parseLong(ctx.pathParam("id"));

        ServicioHospitalario servicio = servicioService.buscarServicioPorId(id);

        if (servicio == null) {
            ctx.status(404).result("Servicio no encontrado con ID: " + id);
            return;
        }

        ctx.json(servicio);
    };
    // PUT /servicios/{id}
    public Handler actualizarServicio = ctx -> {
        try {
            Long id = Long.parseLong(ctx.pathParam("id"));
            ServicioRegistroDTO dto = mapper.readValue(ctx.body(), ServicioRegistroDTO.class);
            servicioService.actualizarServicio(id, dto);
            ctx.status(200).result("Servicio actualizado exitosamente.");
        } catch (Exception e) {
            ctx.status(400).result("Error al actualizar servicio: " + e.getMessage());
        }
    }; 

}
