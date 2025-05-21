package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa un servicio hospitalario en formato simplificado.
 * <p>
 * Se utiliza para listados o selecciones donde solo se necesita el identificador
 * y el nombre del servicio sin detalles adicionales.
 * </p>
 */
public class ServicioSimpleDTO {

    /** Identificador único del servicio hospitalario. */
    private Long id;

    /** Nombre del servicio hospitalario. */
    private String nombre;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public ServicioSimpleDTO() {}

    /**
     * Constructor que inicializa el identificador y nombre del servicio.
     *
     * @param id identificador del servicio
     * @param nombre nombre del servicio
     */
    public ServicioSimpleDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el ID del servicio.
     *
     * @return ID del servicio
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del servicio.
     *
     * @param id ID del servicio
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del servicio.
     *
     * @return nombre del servicio
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del servicio.
     *
     * @param nombre nombre del servicio
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
