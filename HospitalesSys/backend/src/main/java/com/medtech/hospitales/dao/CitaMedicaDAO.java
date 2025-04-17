package com.medtech.hospitales.dao;
import com.medtech.hospitales.models.CitaMedica;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class CitaMedicaDAO {

    private final EntityManager em;

    public CitaMedicaDAO(EntityManager em) {
        this.em = em;
    }

    public void guardar(CitaMedica cita) {
        em.getTransaction().begin();
        em.persist(cita);
        em.getTransaction().commit();
    }

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
