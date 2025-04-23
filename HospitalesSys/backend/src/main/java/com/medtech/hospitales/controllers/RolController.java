package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones relacionadas con los roles de usuario
 * dentro del sistema hospitalario.
 */
public class RolController {

    /**
     * EntityManager utilizado para realizar operaciones de persistencia en la base de datos.
     */
    private final EntityManager em = Persistence
            .createEntityManagerFactory("HospitalesPU")
            .createEntityManager();

    /**
     * Handler que obtiene todos los roles registrados en el sistema.
     * <p>Endpoint: GET /roles</p>
     */
    public Handler listarRoles = ctx -> {
        List<Rol> roles = em.createQuery("SELECT r FROM Rol r", Rol.class).getResultList();
        ctx.json(roles);
    };

    /**
     * Método que agrega las rutas relacionadas con el controlador de roles a la aplicación Javalin.
     *
     * @param app instancia de la aplicación Javalin donde se registrarán las rutas
     */
    public static void addRoutes(Javalin app) {
        RolController controller = new RolController();
        app.get("/roles", controller.listarRoles);
    }
}
