package com.medtech.hospitales.services;

import jakarta.persistence.*;

public class RolService {

    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    public Integer obtenerIdRolPorNombre(String nombreRol) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT r.id FROM Rol r WHERE UPPER(r.nombreRol) = :nombre";
            return em.createQuery(jpql, Integer.class)
                    .setParameter("nombre", nombreRol.toUpperCase())
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }
}
