package com.medtech.hospitales.dao;

import com.medtech.hospitales.models.HeaderFooter;
import jakarta.persistence.EntityManager;

import java.util.List;

public class HeaderFooterDAO {
    private final EntityManager em;

    public HeaderFooterDAO(EntityManager em) {
        this.em = em;
    }

    public List<HeaderFooter> findAll() {
        em.getTransaction().begin();
        List<HeaderFooter> result = em.createQuery("SELECT h FROM HeaderFooter h", HeaderFooter.class).getResultList();
        em.getTransaction().commit();
        return result;
    }

    public void update(HeaderFooter hf) {
        em.getTransaction().begin();
        em.merge(hf);
        em.getTransaction().commit();
    }
}