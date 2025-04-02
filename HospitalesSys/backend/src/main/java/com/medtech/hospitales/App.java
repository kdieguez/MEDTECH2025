package com.medtech.hospitales;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.medtech.hospitales.controllers.*;
import com.medtech.hospitales.dao.HeaderFooterDAO;
import com.medtech.hospitales.services.HeaderFooterService;
import com.medtech.hospitales.utils.JPAUtil;
import com.medtech.hospitales.utils.CustomJsonMapper;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;

public class App {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
            config.jsonMapper(new CustomJsonMapper(objectMapper));
        });

        try {
            System.out.println(objectMapper.writeValueAsString(LocalDate.now()));
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }

        app.options("/*", App::handlePreflight);

        DoctorController doctorController = new DoctorController(JPAUtil.getEntityManager());
        app.post("/doctores/perfil", doctorController.registrarPerfilDoctor());
        app.get("/doctores/verificar-perfil/{id}", doctorController.verificarPerfilDoctor);
        app.get("/doctores", doctorController.listarDoctores);
        app.get("/doctores/{id}", doctorController.detalleDoctor);

        EspecialidadController especialidadController = new EspecialidadController(JPAUtil.getEntityManager());
        app.get("/especialidades", especialidadController.obtenerEspecialidades);
        app.post("/especialidades", especialidadController.agregarEspecialidad);

        LoginController loginController = new LoginController();
        app.post("/login", loginController.login);

        UsuarioController.addRoutes(app);
        CitasController.addRoutes(app);

        HeaderFooterDAO headerFooterDAO = new HeaderFooterDAO(JPAUtil.getEntityManager());
        HeaderFooterService headerFooterService = new HeaderFooterService(headerFooterDAO);
        HeaderFooterController.register(app, headerFooterService);

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
