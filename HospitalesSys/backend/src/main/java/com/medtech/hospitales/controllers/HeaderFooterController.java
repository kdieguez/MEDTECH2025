package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.HeaderFooter;
import com.medtech.hospitales.services.HeaderFooterService;
import io.javalin.Javalin;

import java.util.List;
import java.util.Map;

public class HeaderFooterController {

    public static void register(Javalin app, HeaderFooterService service) {

        app.get("/api/header-footer", ctx -> {
            Map<String, String> map = service.getMapeado();
            ctx.json(map);
        });

        app.get("/api/header-footer/all", ctx -> {
            List<HeaderFooter> all = service.getAll();
            ctx.json(all);
        });

        app.put("/api/header-footer", ctx -> {
            HeaderFooter hf = ctx.bodyAsClass(HeaderFooter.class);
            service.update(hf);
            ctx.result("Actualizado correctamente");
        });
    }
}
