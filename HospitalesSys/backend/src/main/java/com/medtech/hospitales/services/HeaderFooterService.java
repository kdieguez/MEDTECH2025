package com.medtech.hospitales.services;

import com.medtech.hospitales.dao.HeaderFooterDAO;
import com.medtech.hospitales.models.HeaderFooter;

import java.util.List;

public class HeaderFooterService {

    private HeaderFooterDAO headerFooterDAO;

    public HeaderFooterService(HeaderFooterDAO dao) {
        this.headerFooterDAO = dao;
    }

    public List<HeaderFooter> obtenerTodos() {
        return headerFooterDAO.obtenerTodos();
    }
    
}
