package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.dtos.ServicioDTO;
import com.medtech.hospitales.dtos.ServicioRegistroDTO;
import com.medtech.hospitales.dtos.SubcategoriaDTO;
import com.medtech.hospitales.models.ServicioHospitalario;
import com.medtech.hospitales.models.ServicioXDoctor;
import com.medtech.hospitales.models.SubcategoriaServicio;
import com.medtech.hospitales.services.ServicioHospitalarioService;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;
import com.medtech.hospitales.dtos.ServicioDTO;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ServicioHospitalarioController {

    private ServicioHospitalarioService servicioService = null;
    private final ObjectMapper mapper = new ObjectMapper();
    private EntityManager entityManager = null;

    public ServicioHospitalarioController(EntityManager entityManager) {
        this.entityManager = entityManager;
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

    public Handler serviciosPorDoctor = ctx -> {
        Long idDoctor = Long.parseLong(ctx.pathParam("id"));
        List<ServicioXDoctor> relaciones = entityManager.createQuery(
            "SELECT s FROM ServicioXDoctor s WHERE s.doctor.id = :id",
            ServicioXDoctor.class
        )
        .setParameter("id", idDoctor)
        .getResultList();

    
    List<ServicioDTO> servicios = relaciones.stream()
    .map(sxd -> new ServicioDTO(
        sxd.getServicio().getId(),
        sxd.getServicio().getNombre()
    ))
    .collect(Collectors.toList());

    ctx.json(servicios);
};

public Handler subcategoriasPorServicio = ctx -> {
    Long idServicio = Long.parseLong(ctx.pathParam("id"));

    List<SubcategoriaServicio> subcategorias = entityManager.createQuery(
        "SELECT s FROM SubcategoriaServicio s WHERE s.servicio.id = :id",
        SubcategoriaServicio.class
    )
    .setParameter("id", idServicio)
    .getResultList();

    List<SubcategoriaDTO> dtos = subcategorias.stream()
        .map(s -> new SubcategoriaDTO(
            s.getId(),
            s.getNombre(), 
            s.getDescripcion(),
            s.getPrecio()
        ))
        .collect(Collectors.toList());

    ctx.json(dtos);
};


}
