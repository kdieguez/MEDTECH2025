package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.models.SeccionPendiente;
import com.medtech.hospitales.services.SeccionPendienteService;
import io.javalin.http.Context;

import java.util.List;

public class SeccionPendienteController {

    private static final SeccionPendienteService service = new SeccionPendienteService();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void guardar(Context ctx) {
        try {
            SeccionPendiente seccion = mapper.readValue(ctx.body(), SeccionPendiente.class);
            service.guardarCambio(seccion);
            ctx.status(201).result("Cambio guardado correctamente");
        } catch (Exception e) {
            ctx.status(400).result("Error al guardar cambio: " + e.getMessage());
        }
    }

    public static void listarPendientes(Context ctx) {
        List<SeccionPendiente> cambios = service.listarPendientes();
        ctx.json(cambios);
    }

    public static void aprobar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Long idAprobador = Long.parseLong(ctx.queryParam("aprobador"));
        service.aprobarCambio(id, idAprobador);
        ctx.result("Cambio aprobado");
    }

    public static void rechazar(Context ctx) {
        Long id = Long.parseLong(ctx.pathParam("id"));
        Long idAprobador = Long.parseLong(ctx.queryParam("aprobador"));
        String comentario = ctx.queryParam("comentario");
        service.rechazarCambio(id, idAprobador, comentario);
        ctx.result("Cambio rechazado");
    }

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