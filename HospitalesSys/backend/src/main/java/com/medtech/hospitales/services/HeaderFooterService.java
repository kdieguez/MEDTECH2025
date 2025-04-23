package com.medtech.hospitales.services;

import com.medtech.hospitales.dao.HeaderFooterDAO;
import com.medtech.hospitales.models.HeaderFooter;

import java.util.List;

/**
 * Servicio que maneja las operaciones relacionadas con el contenido
 * del header y footer del sistema hospitalario.
 */
public class HeaderFooterService {

    /**
     * DAO para acceso a los datos de header y footer.
     */
    private HeaderFooterDAO headerFooterDAO;

    /**
     * Constructor que recibe el DAO de header/footer.
     *
     * @param dao Instancia de HeaderFooterDAO para operaciones de base de datos.
     */
    public HeaderFooterService(HeaderFooterDAO dao) {
        this.headerFooterDAO = dao;
    }

    /**
     * Obtiene todos los registros de header y footer almacenados.
     *
     * @return lista de objetos HeaderFooter.
     */
    public List<HeaderFooter> obtenerTodos() {
        return headerFooterDAO.obtenerTodos();
    }
}
