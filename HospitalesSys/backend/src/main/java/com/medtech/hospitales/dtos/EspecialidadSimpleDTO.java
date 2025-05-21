package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa una especialidad médica
 * en formato simplificado, utilizado para listados o selecciones rápidas.
 * <p>
 * Contiene únicamente el identificador y el nombre de la especialidad.
 * </p>
 */
public class EspecialidadSimpleDTO {

    /** Identificador único de la especialidad. */
    private Long id;

    /** Nombre de la especialidad médica. */
    private String nombre;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public EspecialidadSimpleDTO() {}

    /**
     * Constructor que inicializa los campos de la especialidad simple.
     *
     * @param id identificador único de la especialidad
     * @param nombre nombre descriptivo de la especialidad
     */
    public EspecialidadSimpleDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    /**
     * Obtiene el identificador de la especialidad.
     *
     * @return ID de la especialidad
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador de la especialidad.
     *
     * @param id ID de la especialidad
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de la especialidad.
     *
     * @return nombre de la especialidad
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la especialidad.
     *
     * @param nombre nombre de la especialidad
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
