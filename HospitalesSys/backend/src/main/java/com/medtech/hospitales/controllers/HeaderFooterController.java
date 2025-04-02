package com.medtech.hospitales.controllers;

import com.medtech.hospitales.services.HeaderFooterService;
import java.util.List;
import com.medtech.hospitales.models.HeaderFooter;
import io.javalin.Javalin;

public class HeaderFooterController {

    public static void register(Javalin app, HeaderFooterService service) {
        app.get("/headerfooter", ctx -> {
            List<HeaderFooter> datos = service.obtenerTodos();
            ctx.json(datos);
        });
    }
}

