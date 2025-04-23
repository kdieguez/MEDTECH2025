package com.medtech.hospitales.dtos;

import java.util.List;

/**
 * DTO (Data Transfer Object) utilizado para registrar un nuevo servicio hospitalario,
 * incluyendo su descripción, subcategorías asociadas y doctores asignados.
 */
public class ServicioRegistroDTO {

    /**
     * Nombre del servicio hospitalario.
     */
    private String nombre;

    /**
     * Descripción detallada del servicio hospitalario.
     */
    private String descripcion;

    /**
     * Lista de subcategorías asociadas al servicio.
     */
    private List<SubcategoriaDTO> subcategorias;

    /**
     * Lista de identificadores de los doctores asignados al servicio.
     */
    private List<Long> idDoctores;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public ServicioRegistroDTO() {}

    /**
     * Constructor que inicializa todos los atributos del servicio.
     *
     * @param nombre nombre del servicio
     * @param descripcion descripción del servicio
     * @param subcategorias lista de subcategorías del servicio
     * @param idDoctores lista de IDs de doctores asociados
     */
    public ServicioRegistroDTO(String nombre, String descripcion, List<SubcategoriaDTO> subcategorias, List<Long> idDoctores) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.subcategorias = subcategorias;
        this.idDoctores = idDoctores;
    }

    /**
     * Obtiene el nombre del servicio hospitalario.
     *
     * @return nombre del servicio
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del servicio hospitalario.
     *
     * @param nombre nombre del servicio
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la descripción del servicio hospitalario.
     *
     * @return descripción del servicio
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción del servicio hospitalario.
     *
     * @param descripcion descripción del servicio
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la lista de subcategorías asociadas al servicio.
     *
     * @return lista de subcategorías
     */
    public List<SubcategoriaDTO> getSubcategorias() {
        return subcategorias;
    }

    /**
     * Establece la lista de subcategorías asociadas al servicio.
     *
     * @param subcategorias lista de subcategorías
     */
    public void setSubcategorias(List<SubcategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }

    /**
     * Obtiene la lista de IDs de doctores asociados al servicio.
     *
     * @return lista de IDs de doctores
     */
    public List<Long> getIdDoctores() {
        return idDoctores;
    }

    /**
     * Establece la lista de IDs de doctores asociados al servicio.
     *
     * @param idDoctores lista de IDs de doctores
     */
    public void setIdDoctores(List<Long> idDoctores) {
        this.idDoctores = idDoctores;
    }
}
