package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.models.SeccionPendiente;
import com.medtech.hospitales.services.SeccionPendienteService;
import io.javalin.http.Context;

import java.util.List;

/**
 * Controlador encargado de gestionar los cambios pendientes en secciones informativas,
 * incluyendo su registro, revisión, aprobación y rechazo por parte de los administradores.
 */
public class SeccionPendienteController {

    /** Servicio que maneja la lógica de negocio para secciones pendientes. */
    private static final SeccionPendienteService service = new SeccionPendienteService();

    /** Mapper utilizado para convertir JSON en objetos Java. */
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Registra un nuevo cambio pendiente de aprobación.
     *
     * @param ctx Contexto HTTP que contiene el cuerpo JSON de {@link SeccionPendiente}
     */
    public static void guardar(Context ctx) {
        try {
            SeccionPendiente seccion = mapper.readValue(ctx.body(), SeccionPendiente.class);
            service.guardarCambio(seccion);
            ctx.status(201).result("Cambio guardado correctamente");
        } catch (Exception e) {
            ctx.status(400).result("Error al guardar cambio: " + e.getMessage());
        }
    }

    /**
     * Lista todos los cambios pendientes que están en espera de aprobación o rechazo.
     *
     * @param ctx Contexto HTTP
     */
    public static void listarPendientes(Context ctx) {
        List<SeccionPendiente> cambios = service.listarPendientes();
        ctx.json(cambios);
    }

    /**
     * Aprueba un cambio pendiente identificado por su ID.
     *
     * @param ctx Contexto HTTP con path param "id" y query param "aprobador"
     */
    public static void aprobar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Long idAprobador = Long.parseLong(ctx.queryParam("aprobador"));
        service.aprobarCambio(id, idAprobador);
        ctx.result("Cambio aprobado");
    }

    /**
     * Rechaza un cambio pendiente, indicando el motivo del rechazo.
     *
     * @param ctx Contexto HTTP con path param "id", query param "aprobador" y "comentario"
     */
    public static void rechazar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Long idAprobador = Long.parseLong(ctx.queryParam("aprobador"));
        String comentario = ctx.queryParam("comentario");
        service.rechazarCambio(id, idAprobador, comentario);
        ctx.result("Cambio rechazado");
    }

    /**
     * Obtiene un cambio pendiente específico por su ID.
     *
     * @param ctx Contexto HTTP con path param "id"
     */
    public static void obtenerPorId(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        SeccionPendiente cambio = service.obtenerPorId(id);
        if (cambio != null) {
            ctx.json(cambio);
        } else {
            ctx.status(404).result("Cambio no encontrado");
        }
    }
}
