package com.medtech.hospitales.controllers;

import com.medtech.hospitales.services.HeaderFooterService;
import java.util.List;
import com.medtech.hospitales.models.HeaderFooter;
import io.javalin.Javalin;

/**
 * Controlador encargado de exponer los datos de Header y Footer del sistema,
 * permitiendo su consulta a través de un endpoint REST.
 */
public class HeaderFooterController {

    /**
     * Registra la ruta necesaria en la aplicación Javalin para consultar los datos
     * del header y footer.
     *
     * @param app instancia de Javalin donde se registrarán las rutas
     * @param service servicio que proporciona el acceso a los datos de HeaderFooter
     */
    public static void register(Javalin app, HeaderFooterService service) {
        app.get("/headerfooter", ctx -> {
            List<HeaderFooter> datos = service.obtenerTodos();
            ctx.json(datos);
        });
    }
}
