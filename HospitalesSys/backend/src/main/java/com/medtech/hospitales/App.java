package com.medtech.hospitales;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.medtech.hospitales.controllers.UsuarioController;
import com.medtech.hospitales.controllers.CitasController;
import com.medtech.hospitales.controllers.InfoDoctorController;

public class App {

    public static void main(String[] args) {

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> {
                cors.add(it -> {
                    it.anyHost();
                });
            });
        });

        app.options("/*", App::handlePreflight);

       UsuarioController.addRoutes(app);
        CitasController.addRoutes(app);
        InfoDoctorController.addRoutes(app);


        app.start(7000);

        System.out.println("Servidor corriendo en: http://localhost:7000");
    }

    private static void handlePreflight(Context ctx) {
        ctx.header("Access-Control-Allow-Origin", "*");
        ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
        ctx.status(200);
    }
}