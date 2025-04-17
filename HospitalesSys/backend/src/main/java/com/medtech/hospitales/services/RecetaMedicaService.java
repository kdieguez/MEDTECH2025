package com.medtech.hospitales.services;

import com.medtech.hospitales.models.CitaMedica;
import com.medtech.hospitales.models.PerfilPaciente;
import com.medtech.hospitales.models.InfoDoctor;
import com.medtech.hospitales.models.RecetaMedicaHeader;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;

public class RecetaMedicaService {

    public void crearEncabezadoReceta(Long idCita) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            CitaMedica cita = em.find(CitaMedica.class, idCita);
            if (cita == null) {
                throw new RuntimeException("No se encontró la cita con ID: " + idCita);
            }

            RecetaMedicaHeader receta = new RecetaMedicaHeader();

            receta.setFechaReceta(LocalDate.now());

            // Código de receta temporal: "PAZ000" + id de la cita
            String codigo = "PAZ000-" + idCita;
            receta.setCodigoReceta(codigo);

            // Relacionarlo con paciente y doctor
            receta.setPaciente(cita.getPaciente());
            receta.setInfoDoctor(cita.getInfoDoctor());

            // Guardarlo
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
