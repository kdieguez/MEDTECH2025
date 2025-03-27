package com.medtech.hospitales.services;

import com.medtech.hospitales.dao.HeaderFooterDAO;
import com.medtech.hospitales.models.HeaderFooter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HeaderFooterService {
    private final HeaderFooterDAO dao;

    public HeaderFooterService(HeaderFooterDAO dao) {
        this.dao = dao;
    }

    public Map<String, String> getMapeado() {
        List<HeaderFooter> lista = dao.findAll();
        Map<String, String> map = new HashMap<>();
        for (HeaderFooter h : lista) {
            map.put(h.getTitulo(), h.getContenido());
        }
        return map;
    }

    public List<HeaderFooter> getAll() {
        return dao.findAll();
    }

    public void update(HeaderFooter hf) {
        dao.update(hf);
    }
}
