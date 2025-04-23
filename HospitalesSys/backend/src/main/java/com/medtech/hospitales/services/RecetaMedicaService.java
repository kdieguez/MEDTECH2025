package com.medtech.hospitales.services;

import com.medtech.hospitales.models.CitaMedica;
import com.medtech.hospitales.models.RecetaMedicaHeader;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

/**
 * Servicio que maneja la creación del encabezado de recetas médicas
 * asociadas a citas médicas en el sistema hospitalario.
 */
public class RecetaMedicaService {

    /**
     * Crea un nuevo encabezado de receta médica basado en una cita existente.
     *
     * @param idCita ID de la cita médica para generar la receta.
     * @throws RuntimeException si ocurre un error al crear la receta.
     */
    public void crearEncabezadoReceta(Long idCita) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            // Buscar la cita médica
            CitaMedica cita = em.find(CitaMedica.class, idCita);
            if (cita == null) {
                throw new RuntimeException("No se encontró la cita con ID: " + idCita);
            }

            // Crear encabezado de receta
            RecetaMedicaHeader receta = new RecetaMedicaHeader();
            receta.setFechaReceta(LocalDate.now());

            // Código de receta temporal basado en el ID de la cita
            String codigo = "PAZ000-" + idCita;
            receta.setCodigoReceta(codigo);

            // Asociar paciente y doctor
            receta.setPaciente(cita.getPaciente());
            receta.setInfoDoctor(cita.getInfoDoctor());

            // Persistir el encabezado de receta
            em.persist(receta);

            em.getTransaction().commit();
            System.out.println("Receta médica encabezado guardado para cita ID: " + idCita);

        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al crear el encabezado de receta médica: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
