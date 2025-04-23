package com.medtech.hospitales.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Clase utilitaria para la gestión de EntityManagerFactory y creación de EntityManager,
 * basada en la unidad de persistencia configurada ("HospitalesPU").
 */
public class JPAUtil {

    /**
     * Instancia única del EntityManagerFactory para la aplicación.
     */
    private static final EntityManagerFactory entityManagerFactory = buildEntityManagerFactory();

    /**
     * Inicializa el EntityManagerFactory a partir de la unidad de persistencia definida.
     *
     * @return EntityManagerFactory creado.
     * @throws ExceptionInInitializerError si falla la creación.
     */
    private static EntityManagerFactory buildEntityManagerFactory() {
        try {
            return Persistence.createEntityManagerFactory("HospitalesPU");
        } catch (Throwable ex) {
            System.err.println("Error al inicializar el EntityManagerFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    /**
     * Obtiene una nueva instancia de EntityManager para operaciones de persistencia.
     *
     * @return Nueva instancia de EntityManager.
     */
    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    /**
     * Cierra el EntityManagerFactory cuando ya no es necesario,
     * liberando los recursos asociados.
     */
    public static void shutdown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
