package com.medtech.hospitales.controllers;

import com.medtech.hospitales.dtos.NuevaEspecialidadDTO;
import com.medtech.hospitales.models.Especialidad;
import io.javalin.http.Handler;
import jakarta.persistence.EntityManager;

import java.util.List;

public class EspecialidadController {

    private EntityManager entityManager;

    public EspecialidadController(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Handler obtenerEspecialidades = ctx -> {
        List<Especialidad> especialidades = entityManager
            .createQuery("SELECT e FROM Especialidad e", Especialidad.class)
            .getResultList();
        ctx.json(especialidades);
    };

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
