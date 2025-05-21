package com.medtech.hospitales.controllers;

import com.medtech.hospitales.models.Rol;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.List;

/**
 * Controlador encargado de exponer los roles de usuario del sistema hospitalario a través de endpoints REST.
 * <p>
 * Este controlador permite consultar todos los roles registrados en la base de datos,
 * los cuales son usados para determinar los permisos y accesos de los usuarios en el sistema.
 * </p>
 */
public class RolController {

    /**
     * EntityManager utilizado para realizar operaciones de persistencia con la base de datos Oracle.
     * Esta instancia es creada directamente desde la unidad de persistencia "HospitalesPU".
     */
    private final EntityManager em = Persistence
            .createEntityManagerFactory("HospitalesPU")
            .createEntityManager();

    /**
     * Handler que devuelve todos los roles de usuario registrados.
     * <p>
     * Endpoint: <code>GET /roles</code>
     * </p>
     */
    public Handler listarRoles = ctx -> {
        List<Rol> roles = em.createQuery("SELECT r FROM Rol r", Rol.class).getResultList();
        ctx.json(roles);
    };

    /**
     * Método estático que agrega las rutas relacionadas con roles a la aplicación Javalin.
     *
     * @param app instancia de la aplicación Javalin donde se registrarán las rutas.
     */
    public static void addRoutes(Javalin app) {
        RolController controller = new RolController();
        app.get("/roles", controller.listarRoles);
    }
}
