package com.medtech.hospitales;

import io.javalin.Javalin;
import io.javalin.http.Context;

import com.medtech.hospitales.controllers.*;
import com.medtech.hospitales.dao.HeaderFooterDAO;
import com.medtech.hospitales.services.HeaderFooterService;
import com.medtech.hospitales.utils.JPAUtil;
import com.medtech.hospitales.utils.CustomJsonMapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class App {

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> it.anyHost()));
            config.jsonMapper(new CustomJsonMapper(objectMapper));
        });

        app.options("/*", App::handlePreflight);

        try {
            System.out.println(objectMapper.writeValueAsString(LocalDate.now()));
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            e.printStackTrace();
        }

        EntityManager em = JPAUtil.getEntityManager();

        DoctorController doctorController = new DoctorController(em);
        app.post("/doctores/perfil", doctorController.registrarPerfilDoctor());
        app.get("/doctores/verificar-perfil/{id}", doctorController.verificarPerfilDoctor);
        app.get("/doctores", doctorController.listarDoctores);
        app.get("/doctores/{id}", doctorController.detalleDoctor);
        app.get("/info-doctores", doctorController.listarDoctoresInfo);
        app.get("/doctores/por-servicio/{id}", doctorController.doctoresPorServicio);

        ServicioHospitalarioController servicioController = new ServicioHospitalarioController(em);
        app.post("/servicios", servicioController.registrarServicio);
        app.get("/servicios", servicioController.listarServicios);
        app.get("/servicios/{id}", servicioController.detalleServicio);
        app.put("/servicios/{id}", servicioController.actualizarServicio);
        app.get("/servicios/por-doctor/{id}", servicioController.serviciosPorDoctor);
        app.get("/servicios/{id}/subcategorias", servicioController.subcategoriasPorServicio);
        app.get("/servicios-hospitalarios", servicioController.serviciosParaSeguros);

        EspecialidadController especialidadController = new EspecialidadController(em);
        app.get("/especialidades", especialidadController.obtenerEspecialidades);
        app.post("/especialidades", especialidadController.agregarEspecialidad);

        LoginController loginController = new LoginController();
        app.post("/login", loginController.login);

        CitaMedicaController citaController = new CitaMedicaController(new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)    
    );
    app.post("/citas", citaController::registrarCita);
    app.get("/citas", citaController::obtenerTodasLasCitas);
    app.get("/citas/horarios-disponibles", citaController::obtenerHorasDisponibles);
    app.get("/citas/mias", citaController::obtenerMisCitas);
    app.post("/formulario-cita", citaController::guardarFormularioCita);
    app.get("/receta/{idCita}", citaController::obtenerDatosReceta);
    app.post("/receta/{id}/crear", citaController::crearRecetaYRetornarId);
    app.post("/receta/{id}/guardar-medicamentos", citaController::guardarMedicamentosPorReceta);
    app.post("/receta/{id}/comentario", citaController::guardarRecetaCompleta);


    MedicamentoController medicamentoController = new MedicamentoController(objectMapper);
    app.post("/medicamentos", medicamentoController::guardarMedicamento);
    app.get("/medicamentos", medicamentoController::obtenerMedicamentos);

        UsuarioController.addRoutes(app);
        RolController.addRoutes(app);
        CargoController.addRoutes(app, em);

        HeaderFooterDAO headerFooterDAO = new HeaderFooterDAO(em);
        HeaderFooterService headerFooterService = new HeaderFooterService(headerFooterDAO);
        HeaderFooterController.register(app, headerFooterService);

        PaginaController paginaController = new PaginaController(objectMapper);

        app.get("/paginas", paginaController::obtenerTodasLasPaginas);
        app.get("/paginas/{idPagina}/secciones", paginaController::obtenerSeccionesDePagina);
        app.put("/secciones/{idSeccion}", paginaController::actualizarSeccion);
        app.post("/paginas/{idPagina}/secciones", paginaController::agregarSeccion);
        app.delete("/secciones/{idSeccion}", paginaController::eliminarSeccion);
        

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