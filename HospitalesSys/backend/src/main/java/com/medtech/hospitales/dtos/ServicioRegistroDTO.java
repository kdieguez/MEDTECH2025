package com.medtech.hospitales.dtos;

import java.util.List;

/**
 * DTO (Data Transfer Object) utilizado para registrar un nuevo servicio hospitalario en el sistema.
 * <p>
 * Contiene el nombre del servicio, una descripción detallada, una lista de subcategorías médicas
 * asociadas y los identificadores de los doctores que estarán asignados al servicio.
 * </p>
 */
public class ServicioRegistroDTO {

    /** Nombre del servicio hospitalario (ej. "Consulta Externa", "Laboratorio Clínico"). */
    private String nombre;

    /** Descripción completa del servicio (objetivo, alcance, etc.). */
    private String descripcion;

    /** Lista de subcategorías médicas asociadas a este servicio. */
    private List<SubcategoriaDTO> subcategorias;

    /** Lista de IDs de doctores que estarán asignados al servicio. */
    private List<Long> idDoctores;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public ServicioRegistroDTO() {}

    /**
     * Constructor que inicializa todos los atributos del servicio hospitalario.
     *
     * @param nombre nombre del servicio
     * @param descripcion descripción detallada del servicio
     * @param subcategorias lista de subcategorías del servicio
     * @param idDoctores lista de identificadores de doctores asignados
     */
    public ServicioRegistroDTO(String nombre, String descripcion,
                               List<SubcategoriaDTO> subcategorias,
                               List<Long> idDoctores) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.subcategorias = subcategorias;
        this.idDoctores = idDoctores;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<SubcategoriaDTO> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<SubcategoriaDTO> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public List<Long> getIdDoctores() {
        return idDoctores;
    }

    public void setIdDoctores(List<Long> idDoctores) {
        this.idDoctores = idDoctores;
    }
}
