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

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador encargado de gestionar los servicios hospitalarios,
 * incluyendo su registro, consulta, actualización y obtención de subcategorías.
 */
public class ServicioHospitalarioController {

    /**
     * Servicio encargado de la lógica de negocio para servicios hospitalarios.
     */
    private ServicioHospitalarioService servicioService = null;

    /**
     * Mapper utilizado para la conversión entre objetos JSON y clases Java.
     */
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * EntityManager utilizado para operaciones de persistencia en la base de datos.
     */
    private EntityManager entityManager = null;

    /**
     * Constructor del controlador que recibe un EntityManager.
     *
     * @param entityManager instancia de EntityManager
     */
    public ServicioHospitalarioController(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.servicioService = new ServicioHospitalarioService(entityManager);
    }

    /**
     * Handler que registra un nuevo servicio hospitalario en el sistema.
     * 
     * @param ctx contexto de Javalin que contiene los datos del servicio a registrar
     */
    public Handler registrarServicio = ctx -> {
        try {
            ServicioRegistroDTO dto = mapper.readValue(ctx.body(), ServicioRegistroDTO.class);
            servicioService.registrarServicio(dto);
            ctx.status(201).result("Servicio hospitalario registrado exitosamente.");
        } catch (Exception e) {
            ctx.status(400).result("Error al registrar servicio: " + e.getMessage());
        }
    };

    /**
     * Handler que lista todos los servicios hospitalarios registrados.
     * 
     * @param ctx contexto de Javalin
     */
    public Handler listarServicios = ctx -> {
        List<ServicioHospitalario> servicios = servicioService.listarServicios();
        ctx.json(servicios);
    };

    /**
     * Handler que obtiene el detalle de un servicio hospitalario específico.
     * 
     * @param ctx contexto de Javalin con el ID del servicio
     */
    public Handler detalleServicio = ctx -> {
        Long id = Long.parseLong(ctx.pathParam("id"));

        ServicioHospitalario servicio = servicioService.buscarServicioPorId(id);

        if (servicio == null) {
            ctx.status(404).result("Servicio no encontrado con ID: " + id);
            return;
        }

        ctx.json(servicio);
    };

    /**
     * Handler que actualiza un servicio hospitalario existente.
     * 
     * @param ctx contexto de Javalin con el ID del servicio y los nuevos datos
     */
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

    /**
     * Handler que obtiene todos los servicios asociados a un doctor específico.
     * 
     * @param ctx contexto de Javalin con el ID del doctor
     */
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

    /**
     * Handler que obtiene todas las subcategorías asociadas a un servicio hospitalario.
     * 
     * @param ctx contexto de Javalin con el ID del servicio
     */
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
