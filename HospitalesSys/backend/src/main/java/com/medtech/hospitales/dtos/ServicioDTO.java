package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa un servicio hospitalario
 * en formato simplificado, incluyendo su identificador y nombre.
 */
public class ServicioDTO {

    /**
     * Identificador único del servicio hospitalario.
     */
    private Long id;

    /**
     * Nombre del servicio hospitalario.
     */
    private String nombre;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public ServicioDTO() {
    }

    /**
     * Constructor que inicializa el servicio con su ID y nombre.
     *
     * @param id ID del servicio
     * @param nombre nombre del servicio
     */
    public ServicioDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID del servicio hospitalario.
     *
     * @return ID del servicio
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del servicio hospitalario.
     *
     * @param id ID del servicio
     */
    public void setId(Long id) {
        this.id = id;
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
}
