package com.medtech.hospitales.dao;

import com.medtech.hospitales.models.HeaderFooter;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad {@link HeaderFooter}.
 * 
 * Permite realizar operaciones de consulta sobre los datos de header y footer del sistema.
 */
public class HeaderFooterDAO {

    /**
     * EntityManager utilizado para realizar operaciones de persistencia.
     */
    private final EntityManager em;

    /**
     * Constructor del DAO que recibe un EntityManager.
     *
     * @param em instancia de EntityManager
     */
    public HeaderFooterDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Obtiene todos los registros de header y footer almacenados en la base de datos.
     *
     * @return lista de objetos {@link HeaderFooter}
     */
    public List<HeaderFooter> obtenerTodos() {
        return em.createQuery("SELECT hf FROM HeaderFooter hf", HeaderFooter.class)
                 .getResultList();
    }
}
