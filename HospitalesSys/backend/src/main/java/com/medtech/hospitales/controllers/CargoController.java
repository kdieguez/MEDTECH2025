package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Cargo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los cargos del hospital.
 * Utiliza Javalin para exponer endpoints REST y JPA para interactuar con la base de datos.
 */
public class CargoController {

    /**
     * EntityManager utilizado para realizar operaciones de persistencia en la base de datos.
     */
    private final EntityManager em = Persistence
            .createEntityManagerFactory("HospitalesPU")
            .createEntityManager();

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
     */
    public static void addRoutes(Javalin app) {
        CargoController controller = new CargoController();
        app.get("/cargos", controller.listarCargos);
    }

}
