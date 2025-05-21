package com.medtech.hospitales.dao;

import com.medtech.hospitales.models.HeaderFooter;
import jakarta.persistence.EntityManager;
import java.util.List;

/**
 * DAO (Data Access Object) para la entidad {@link HeaderFooter}.
 * <p>
 * Encargado de gestionar las operaciones de consulta relacionadas con los elementos de encabezado y pie de página del sistema.
 * </p>
 */
public class HeaderFooterDAO {

    /**
     * EntityManager utilizado para las operaciones de base de datos.
     */
    private final EntityManager em;

    /**
     * Constructor que recibe una instancia de EntityManager para inicializar el DAO.
     *
     * @param em instancia de EntityManager
     */
    public HeaderFooterDAO(EntityManager em) {
        this.em = em;
    }

    /**
     * Obtiene todos los registros de header y footer almacenados en la base de datos.
     *
     * @return lista de objetos {@link HeaderFooter} encontrados
     */
    public List<HeaderFooter> obtenerTodos() {
        return em.createQuery("SELECT hf FROM HeaderFooter hf", HeaderFooter.class)
                 .getResultList();
    }
}
