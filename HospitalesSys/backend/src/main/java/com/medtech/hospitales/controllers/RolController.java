package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;

public class RolController {

    private final EntityManager em = Persistence
            .createEntityManagerFactory("HospitalesPU")
            .createEntityManager();

    public Handler listarRoles = ctx -> {
        List<Rol> roles = em.createQuery("SELECT r FROM Rol r", Rol.class).getResultList();
        ctx.json(roles);
    };
    
    public static void addRoutes(Javalin app) {
    RolController controller = new RolController();
    app.get("/roles", controller.listarRoles);
    }
}
