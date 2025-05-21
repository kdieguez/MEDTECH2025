package com.medtech.hospitales.dao;

import com.medtech.hospitales.models.CitaMedica;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad {@link CitaMedica}.
 * <p>
 * Encargado de gestionar las operaciones de persistencia y consulta relacionadas a citas médicas.
 * </p>
 */
public class CitaMedicaDAO {

    /**
     * EntityManager utilizado para las operaciones de persistencia.
     */
    private final EntityManager em;

    /**
     * Constructor que inicializa el DAO con una instancia de EntityManager.
     *
     * @param em instancia de EntityManager
     */
    public CitaMedicaDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Persiste una nueva cita médica en la base de datos.
     *
     * @param cita objeto de tipo {@link CitaMedica} a guardar
     */
    public void guardar(CitaMedica cita) {
        em.getTransaction().begin();
        em.persist(cita);
        em.getTransaction().commit();
    }

    /**
     * Obtiene todas las citas médicas de un doctor en un día específico,
     * dentro del horario de atención comprendido entre 08:00 y 16:30 horas.
     *
     * @param idDoctor ID del doctor a consultar
     * @param dia fecha para la cual se desean obtener las citas
     * @return lista de objetos {@link CitaMedica} correspondientes al doctor y fecha indicados
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
