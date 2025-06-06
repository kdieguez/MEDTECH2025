package com.medtech.hospitales.services;

import com.medtech.hospitales.dtos.MedicamentoRecetadoDTO;
import com.medtech.hospitales.models.CitaMedica;
import com.medtech.hospitales.models.Medicamento;
import com.medtech.hospitales.models.MedicamentoRecetado;
import com.medtech.hospitales.models.RecetaMedica;
import com.medtech.hospitales.utils.JPAUtil;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Servicio que maneja las operaciones completas relacionadas con recetas médicas,
 * incluyendo creación, actualización y gestión de medicamentos recetados.
 */
public class RecetaMedicaCompletaService {

    /**
     * Actualiza las anotaciones de una receta médica existente.
     *
     * @param idRM        ID de la receta médica
     * @param anotaciones Comentarios o anotaciones a guardar en la receta
     */
    public void guardarReceta(Long idRM, String anotaciones) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            RecetaMedica receta = em.find(RecetaMedica.class, idRM);
            if (receta == null) {
                throw new RuntimeException("No se encontró receta con ID: " + idRM);
            }
            receta.setAnotaciones(anotaciones);
            em.merge(receta);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar comentario de receta: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Guarda la lista de medicamentos recetados para una receta médica,
     * evitando duplicados y actualizando el total de la receta con un procedimiento almacenado.
     *
     * @param idRM  ID de la receta médica
     * @param lista Lista de medicamentos recetados DTO a guardar
     */
    public void guardarMedicamentosPorReceta(Long idRM, List<MedicamentoRecetadoDTO> lista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            RecetaMedica receta = em.find(RecetaMedica.class, idRM);
            if (receta == null) {
                throw new RuntimeException("No se encontró receta con ID: " + idRM);
            }

            for (MedicamentoRecetadoDTO dto : lista) {
                Medicamento medicamento = em.find(Medicamento.class, dto.getIdMedicamento());
                if (medicamento == null) continue;

                // Validar si ya existe este medicamento para esta receta
                Long existe = em.createQuery("""
                    SELECT COUNT(mr) FROM MedicamentoRecetado mr
                    WHERE mr.receta.id = :idRM AND mr.medicamento.id = :idMed
                """, Long.class)
                .setParameter("idRM", idRM)
                .setParameter("idMed", dto.getIdMedicamento())
                .getSingleResult();

                if (existe > 0) continue;

                MedicamentoRecetado nuevo = new MedicamentoRecetado();
                nuevo.setReceta(receta);
                nuevo.setMedicamento(medicamento);
                nuevo.setDosis(dto.getDosis());
                nuevo.setFrecuencia(dto.getFrecuencia());
                nuevo.setDuracion(dto.getDuracion());

                em.persist(nuevo);
            }

            // Llamar procedimiento almacenado para calcular total
            em.createNativeQuery("BEGIN CALCULAR_TOTAL_RECETA(:idRM); END;")
              .setParameter("idRM", idRM)
              .executeUpdate();

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al guardar medicamentos: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    /**
     * Crea una receta médica vacía asociada a una cita médica,
     * verificando que no exista previamente para la misma cita.
     *
     * @param idCita ID de la cita médica
     * @return ID de la receta médica creada
     */
    public Long crearRecetaPorCita(Long idCita) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            CitaMedica cita = em.find(CitaMedica.class, idCita);
            if (cita == null) {
                throw new RuntimeException("Cita no encontrada con ID: " + idCita);
            }

            RecetaMedica existente = em.createQuery("""
                    SELECT r FROM RecetaMedica r WHERE r.citaMedica.id = :idCita
                """, RecetaMedica.class)
                .setParameter("idCita", idCita)
                .setMaxResults(1)
                .getResultStream()
                .findFirst()
                .orElse(null);

            if (existente != null) {
                throw new RuntimeException("Ya existe una receta médica para esta cita.");
            }

            RecetaMedica nueva = new RecetaMedica();
            nueva.setCitaMedica(cita);
            nueva.setAnotaciones(null);
            nueva.setCodigoReceta("TEMP"); // Evita null temporalmente

            em.persist(nueva);
            em.flush();

            nueva.setCodigoReceta("HOSP-SEG-" + nueva.getId());
            em.merge(nueva);

            em.getTransaction().commit();
            return nueva.getId();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw new RuntimeException("Error al crear receta por cita: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }
}
