package com.medtech.hospitales.dao;

import com.medtech.hospitales.models.CitaMedica;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad {@link CitaMedica}.
 * 
 * Gestiona operaciones de persistencia relacionadas a las citas médicas en la base de datos.
 */
public class CitaMedicaDAO {

    /**
     * EntityManager utilizado para realizar operaciones de persistencia.
     */
    private final EntityManager em;

    /**
     * Constructor del DAO que recibe un EntityManager.
     *
     * @param em instancia de EntityManager
     */
    public CitaMedicaDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Guarda una nueva cita médica en la base de datos.
     *
     * @param cita objeto {@link CitaMedica} a persistir
     */
    public void guardar(CitaMedica cita) {
        em.getTransaction().begin();
        em.persist(cita);
        em.getTransaction().commit();
    }

    /**
     * Obtiene todas las citas médicas de un doctor para un día específico,
     * en el horario comprendido entre las 08:00 y las 16:30 horas.
     *
     * @param idDoctor ID del doctor
     * @param dia día para el cual se desean obtener las citas
     * @return lista de citas médicas encontradas
     */
    public List<CitaMedica> obtenerCitasDelDia(Long idDoctor, LocalDate dia) {
        LocalDateTime inicio = dia.atTime(8, 0);
        LocalDateTime fin = dia.atTime(16, 30);

        TypedQuery<CitaMedica> query = em.createQuery(
            "SELECT c FROM CitaMedica c WHERE c.idDoctor = :idDoctor AND c.fechaHora BETWEEN :inicio AND :fin",
            CitaMedica.class
        );
        query.setParameter("idDoctor", idDoctor);
        query.setParameter("inicio", inicio);
        query.setParameter("fin", fin);
        return query.getResultList();
    }
}
