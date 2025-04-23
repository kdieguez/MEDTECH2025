package com.medtech.hospitales.controllers;

import com.medtech.hospitales.dtos.NuevaEspecialidadDTO;
import com.medtech.hospitales.models.Especialidad;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Controlador encargado de manejar las operaciones relacionadas con las especialidades médicas,
 * incluyendo la creación de nuevas especialidades y la obtención del listado de especialidades existentes.
 */
public class EspecialidadController {

    /**
     * EntityManager utilizado para la interacción con la base de datos.
     */
    private EntityManager entityManager;

    /**
     * Constructor que inicializa el controlador con un EntityManager.
     *
     * @param entityManager instancia de EntityManager
     */
    public EspecialidadController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * Handler que obtiene todas las especialidades médicas registradas en el sistema.
     * <p>Endpoint: GET /especialidades</p>
     */
    public Handler obtenerEspecialidades = ctx -> {
        List<Especialidad> especialidades = entityManager
            .createQuery("SELECT e FROM Especialidad e", Especialidad.class)
            .getResultList();
        ctx.json(especialidades);
    };

    /**
     * Handler que agrega una nueva especialidad médica al sistema a partir de los datos enviados.
     * <p>Endpoint: POST /especialidades</p>
     */
    public Handler agregarEspecialidad = ctx -> {
        NuevaEspecialidadDTO dto = ctx.bodyAsClass(NuevaEspecialidadDTO.class);

        Especialidad nueva = new Especialidad();
        nueva.setNombre(dto.getNombre());

        entityManager.getTransaction().begin();
        System.out.println("Recibido: " + nueva.getNombre());
        entityManager.persist(nueva);
        entityManager.getTransaction().commit();

        ctx.status(201).result("Especialidad creada exitosamente.");
    };
}
