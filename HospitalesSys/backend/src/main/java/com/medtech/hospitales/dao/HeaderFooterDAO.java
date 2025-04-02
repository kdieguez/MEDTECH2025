package com.medtech.hospitales.dao;

import com.medtech.hospitales.models.HeaderFooter;
import jakarta.persistence.EntityManager;
import java.util.List;

public class HeaderFooterDAO {

    private final EntityManager em;

    public HeaderFooterDAO(EntityManager em) {
        this.em = em;
    }

    public List<HeaderFooter> obtenerTodos() {
        return em.createQuery("SELECT hf FROM HeaderFooter hf", HeaderFooter.class)
                 .getResultList();
    }    
}
