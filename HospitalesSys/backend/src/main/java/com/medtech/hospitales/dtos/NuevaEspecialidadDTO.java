package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) utilizado para la creación de una nueva especialidad médica en el sistema.
 * <p>
 * Contiene únicamente el nombre de la especialidad a registrar.
 * </p>
 */
public class NuevaEspecialidadDTO {

    /** Nombre de la nueva especialidad médica. */
    private String nombre;

    /**
     * Constructor vacío requerido para frameworks de serialización/deserialización.
     */
    public NuevaEspecialidadDTO() {}

    /**
     * Constructor que inicializa el nombre de la nueva especialidad.
     *
     * @param nombre nombre de la especialidad
     */
    public NuevaEspecialidadDTO(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el nombre de la nueva especialidad.
     *
     * @return nombre de la especialidad
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la nueva especialidad.
     *
     * @param nombre nombre de la especialidad
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
