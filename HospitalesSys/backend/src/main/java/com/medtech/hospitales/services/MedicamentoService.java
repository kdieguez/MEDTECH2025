package com.medtech.hospitales.services;

import com.medtech.hospitales.models.Medicamento;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;

/**
 * Servicio que maneja las operaciones relacionadas con el registro de medicamentos
 * en el sistema hospitalario.
 */
public class MedicamentoService {

    /**
     * Guarda un nuevo medicamento en la base de datos.
     *
     * @param medicamento Objeto de tipo Medicamento que se desea guardar.
     */
    public void guardarMedicamento(Medicamento medicamento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(medicamento);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar medicamento", e);
        } finally {
            em.close();
        }
    }
}
