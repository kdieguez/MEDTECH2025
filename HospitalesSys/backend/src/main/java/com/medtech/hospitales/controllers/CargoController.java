package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Cargo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;

public class CargoController {

    private final EntityManager em = Persistence
            .createEntityManagerFactory("HospitalesPU")
            .createEntityManager();

    public Handler listarCargos = ctx -> {
        List<Cargo> cargos = em.createQuery("SELECT c FROM Cargo c", Cargo.class).getResultList();
        ctx.json(cargos);
    };

    public static void addRoutes(Javalin app) {
    CargoController controller = new CargoController();
    app.get("/cargos", controller.listarCargos);
    }

}
