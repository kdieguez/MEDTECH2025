package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Cargo;
import jakarta.persistence.EntityManager;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los cargos del hospital.
 * Utiliza Javalin para exponer endpoints REST y JPA para interactuar con la base de datos.
 */
public class CargoController {

    private EntityManager em = null;

    /**
     * Constructor que permite inyectar el EntityManager, facilitando pruebas unitarias.
     *
     * @param em instancia de EntityManager para operaciones de persistencia
     */
    public CargoController(EntityManager em) {
        this.em = em;
    }

    /**
     * Handler de Javalin que permite listar todos los cargos registrados en la base de datos.
     * <p>
     * Endpoint: GET /cargos
     * </p>
     */
    public Handler listarCargos = ctx -> {
        List<Cargo> cargos = em.createQuery("SELECT c FROM Cargo c", Cargo.class).getResultList();
        ctx.json(cargos);
    };

    /**
     * Agrega las rutas relacionadas con el controlador de cargos a la aplicación Javalin.
     *
     * @param app instancia de la aplicación Javalin donde se registrarán las rutas
     * @param em  instancia de EntityManager que se utilizará para persistencia
     */
    public static void addRoutes(Javalin app, EntityManager em) {
        CargoController controller = new CargoController(em);
        app.get("/cargos", controller.listarCargos);
    }
}
