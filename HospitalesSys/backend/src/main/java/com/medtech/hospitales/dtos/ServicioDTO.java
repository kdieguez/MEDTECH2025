package com.medtech.hospitales.dtos;

import java.util.List;

/**
 * DTO (Data Transfer Object) que representa un servicio hospitalario completo,
 * utilizado principalmente para integraciones con el sistema de seguros.
 * <p>
 * Incluye información del servicio, subcategoría asociada, precio y doctores disponibles.
 * </p>
 */
public class ServicioDTO {

    /** Identificador único del servicio hospitalario. */
    private Long id_servicio;

    /** Nombre del servicio (ej. Consulta general, Laboratorio). */
    private String nombre_servicio;

    /** Descripción general del servicio. */
    private String descripcion_servicio;

    /** Identificador único de la subcategoría del servicio. */
    private Long id_subcategoria;

    /** Nombre de la subcategoría (ej. Cardiología, Pediatría). */
    private String nombre_subcategoria;

    /** Descripción detallada de la subcategoría. */
    private String descripcion_subcategoria;

    /** Precio asociado a la subcategoría del servicio. */
    private Double precio;

    /** Lista de IDs de doctores que brindan este servicio. */
    private List<Long> id_info_doctor;

    /**
     * Constructor vacío requerido para serialización/deserialización automática.
     */
    public ServicioDTO() {}

    /**
     * Constructor completo para inicializar todos los campos del servicio.
     *
     * @param id_servicio ID del servicio
     * @param nombre_servicio nombre del servicio
     * @param descripcion_servicio descripción del servicio
     * @param id_subcategoria ID de la subcategoría
     * @param nombre_subcategoria nombre de la subcategoría
     * @param descripcion_subcategoria descripción de la subcategoría
     * @param precio precio del servicio
     * @param id_info_doctor lista de IDs de doctores asociados
     */
    public ServicioDTO(Long id_servicio, String nombre_servicio, String descripcion_servicio,
                       Long id_subcategoria, String nombre_subcategoria, String descripcion_subcategoria,
                       Double precio, List<Long> id_info_doctor) {
        this.id_servicio = id_servicio;
        this.nombre_servicio = nombre_servicio;
        this.descripcion_servicio = descripcion_servicio;
        this.id_subcategoria = id_subcategoria;
        this.nombre_subcategoria = nombre_subcategoria;
        this.descripcion_subcategoria = descripcion_subcategoria;
        this.precio = precio;
        this.id_info_doctor = id_info_doctor;
    }

    public Long getId_servicio() {
        return id_servicio;
    }

    public void setId_servicio(Long id_servicio) {
        this.id_servicio = id_servicio;
    }

    public String getNombre_servicio() {
        return nombre_servicio;
    }

    public void setNombre_servicio(String nombre_servicio) {
        this.nombre_servicio = nombre_servicio;
    }

    public String getDescripcion_servicio() {
        return descripcion_servicio;
    }

    public void setDescripcion_servicio(String descripcion_servicio) {
        this.descripcion_servicio = descripcion_servicio;
    }

    public Long getId_subcategoria() {
        return id_subcategoria;
    }

    public void setId_subcategoria(Long id_subcategoria) {
        this.id_subcategoria = id_subcategoria;
    }

    public String getNombre_subcategoria() {
        return nombre_subcategoria;
    }

    public void setNombre_subcategoria(String nombre_subcategoria) {
        this.nombre_subcategoria = nombre_subcategoria;
    }

    public String getDescripcion_subcategoria() {
        return descripcion_subcategoria;
    }

    public void setDescripcion_subcategoria(String descripcion_subcategoria) {
        this.descripcion_subcategoria = descripcion_subcategoria;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public List<Long> getId_info_doctor() {
        return id_info_doctor;
    }

    public void setId_info_doctor(List<Long> id_info_doctor) {
        this.id_info_doctor = id_info_doctor;
    }
}
