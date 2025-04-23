package com.medtech.hospitales.services;

import jakarta.persistence.*;

/**
 * Servicio que maneja operaciones relacionadas con los roles del sistema hospitalario.
 */
public class RolService {

    /**
     * EntityManagerFactory para gestionar las conexiones a la base de datos.
     */
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("HospitalesPU");

    /**
     * Obtiene el ID de un rol dado su nombre.
     *
     * @param nombreRol Nombre del rol que se desea buscar.
     * @return ID del rol si existe, o null si no se encuentra.
     */
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
