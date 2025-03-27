package com.medtech.hospitales.services;

import com.medtech.hospitales.models.HeaderFooter;
import jakarta.persistence.*;

import java.util.List;

public class HeaderFooterService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    // Obtener todos los registros
    public List<HeaderFooter> obtenerTodos() {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT hf FROM HeaderFooter hf";
            return em.createQuery(jpql, HeaderFooter.class).getResultList();
        } finally {
            em.close();
        }
    }

    // Obtener registro por título (insensible a mayúsculas)
    public HeaderFooter obtenerPorTitulo(String titulo) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT hf FROM HeaderFooter hf WHERE LOWER(hf.titulo) = LOWER(:titulo)";
            return em.createQuery(jpql, HeaderFooter.class)
                     .setParameter("titulo", titulo.toLowerCase()) // 🔧 Aquí está la corrección clave
                     .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Actualizar un registro dado su id
    public void actualizarHeaderFooter(Long id, HeaderFooter hfActualizado) {
        EntityManager em = emf.createEntityManager();
        try {
            HeaderFooter hfExistente = em.find(HeaderFooter.class, id);
            if (hfExistente != null) {
                em.getTransaction().begin();
                hfExistente.setTitulo(hfActualizado.getTitulo());
                hfExistente.setContenido(hfActualizado.getContenido());
                em.merge(hfExistente);
                em.getTransaction().commit();
            }
        } finally {
            em.close();
        }
    }
}
