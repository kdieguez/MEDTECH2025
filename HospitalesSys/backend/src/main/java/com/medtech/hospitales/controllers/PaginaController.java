package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.models.Pagina;
import com.medtech.hospitales.models.SeccionPagina;
import com.medtech.hospitales.services.PaginaService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

public class PaginaController {

    private final PaginaService paginaService = new PaginaService();
    private final ObjectMapper objectMapper;

    public PaginaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void obtenerTodasLasPaginas(Context ctx) {
        List<Pagina> paginas = paginaService.obtenerTodasLasPaginas();
        ctx.json(paginas);
    }

    public void obtenerSeccionesDePagina(Context ctx) {
        Long idPagina = Long.parseLong(ctx.pathParam("idPagina"));
        List<SeccionPagina> secciones = paginaService.obtenerSeccionesDePagina(idPagina);
        ctx.json(secciones);
    }

    public void actualizarSeccion(Context ctx) {
        try {
            Long idSeccion = Long.parseLong(ctx.pathParam("idSeccion"));
            Map<String, Object> datos = objectMapper.readValue(ctx.body(), Map.class);

            String nuevoTitulo = (String) datos.get("titulo");
            String nuevoContenido = (String) datos.get("contenido");
            String nuevaImagenUrl = (String) datos.get("imagenUrl");

            paginaService.actualizarSeccion(idSeccion, nuevoTitulo, nuevoContenido, nuevaImagenUrl);

            ctx.status(200).json(Map.of("mensaje", "Sección actualizada exitosamente"));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Error al actualizar sección: " + e.getMessage()));
        }
    }

    public void agregarSeccion(Context ctx) {
        try {
            Long idPagina = Long.parseLong(ctx.pathParam("idPagina"));
            Map<String, Object> datos = objectMapper.readValue(ctx.body(), Map.class);

            String titulo = (String) datos.get("titulo");
            String contenido = (String) datos.get("contenido");
            String imagenUrl = (String) datos.get("imagenUrl");
            Integer orden = datos.get("orden") != null ? (Integer) datos.get("orden") : 1;

            paginaService.agregarSeccion(idPagina, titulo, contenido, imagenUrl, orden);

            ctx.status(201).json(Map.of("mensaje", "Sección agregada exitosamente"));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Error al agregar sección: " + e.getMessage()));
        }
    }

    public void eliminarSeccion(Context ctx) {
        try {
            Long idSeccion = Long.parseLong(ctx.pathParam("idSeccion"));
            paginaService.eliminarSeccion(idSeccion);

            ctx.status(200).json(Map.of("mensaje", "Sección eliminada exitosamente"));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Error al eliminar sección: " + e.getMessage()));
        }
    }
}