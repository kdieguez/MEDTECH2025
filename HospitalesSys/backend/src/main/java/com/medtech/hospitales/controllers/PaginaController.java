package com.medtech.hospitales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medtech.hospitales.models.Pagina;
import com.medtech.hospitales.models.SeccionPagina;
import com.medtech.hospitales.services.PaginaService;
import io.javalin.http.Context;

import java.util.List;
import java.util.Map;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con las páginas informativas
 * y sus respectivas secciones dentro del sistema hospitalario.
 */
public class PaginaController {

    /**
     * Servicio encargado de la lógica de negocio para páginas y secciones.
     */
    private final PaginaService paginaService = new PaginaService();

    /**
     * Mapper utilizado para la conversión entre objetos JSON y clases Java.
     */
    private final ObjectMapper objectMapper;

    /**
     * Constructor del controlador que recibe un ObjectMapper para deserialización de JSON.
     *
     * @param objectMapper instancia de ObjectMapper
     */
    public PaginaController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Obtiene todas las páginas informativas registradas en el sistema.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y la respuesta
     */
    public void obtenerTodasLasPaginas(Context ctx) {
        List<Pagina> paginas = paginaService.obtenerTodasLasPaginas();
        ctx.json(paginas);
    }

    /**
     * Obtiene todas las secciones pertenecientes a una página específica.
     *
     * @param ctx contexto de Javalin que contiene la solicitud y la respuesta
     */
    public void obtenerSeccionesDePagina(Context ctx) {
        Long idPagina = Long.parseLong(ctx.pathParam("idPagina"));
        List<SeccionPagina> secciones = paginaService.obtenerSeccionesDePagina(idPagina);
        ctx.json(secciones);
    }

    /**
     * Actualiza una sección existente de una página informativa.
     *
     * @param ctx contexto de Javalin que contiene el ID de la sección y los nuevos datos
     */
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

    /**
     * Agrega una nueva sección a una página informativa existente.
     *
     * @param ctx contexto de Javalin que contiene el ID de la página y los datos de la nueva sección
     */
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

    /**
     * Elimina una sección específica de una página informativa.
     *
     * @param ctx contexto de Javalin que contiene el ID de la sección a eliminar
     */
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
