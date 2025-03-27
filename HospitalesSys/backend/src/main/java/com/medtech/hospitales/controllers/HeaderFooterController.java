
package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.HeaderFooter;
import com.medtech.hospitales.services.HeaderFooterService;
import io.javalin.Javalin;
import io.javalin.http.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class HeaderFooterController {

    private final HeaderFooterService hfService = new HeaderFooterService();

    public static void addRoutes(Javalin app) {
        HeaderFooterController controller = new HeaderFooterController();

        app.get("/header-footer", controller::obtenerTodos);
        app.get("/header-footer/:titulo", controller::obtenerPorTitulo);     
        app.put("/header-footer/{id}", controller::actualizarHeaderFooter);
        app.get("/footer", controller::obtenerFooter);
        app.get("/header", controller::obtenerHeader);

    }

    public void obtenerTodos(Context ctx) {
        List<HeaderFooter> lista = hfService.obtenerTodos();
        ctx.json(lista);
    }

public void obtenerPorTitulo(Context ctx) {
    String titulo = ctx.pathParam("titulo");
    HeaderFooter hf = hfService.obtenerPorTitulo(titulo);

    if (hf != null) {
        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("contenido", hf.getContenido());
        ctx.json(respuesta);
    } else {
        ctx.status(404).result("Registro no encontrado para el título: " + titulo);
    }
}

    public void actualizarHeaderFooter(Context ctx) {
        Long id = Long.valueOf(ctx.pathParam("id"));
        HeaderFooter hfActualizado = ctx.bodyAsClass(HeaderFooter.class);
        hfService.actualizarHeaderFooter(id, hfActualizado);
        ctx.result("Registro actualizado exitosamente");
    }
    // GET /footer
    public void obtenerFooter(Context ctx) {
        List<HeaderFooter> footer = hfService.obtenerTodos()
            .stream()
            .filter(item ->
                !item.getTitulo().equalsIgnoreCase("LOGO") &&
                !item.getTitulo().equalsIgnoreCase("TITULO")
            )
            .toList();
        ctx.json(footer);
    }
    public void obtenerHeader(Context ctx) {
        List<HeaderFooter> header = hfService.obtenerTodos()
            .stream()
            .filter(item ->
                item.getTitulo().equalsIgnoreCase("LOGO") ||
                item.getTitulo().equalsIgnoreCase("TITULO")
            )
            .toList();
    
        ctx.json(header);
    }
}
