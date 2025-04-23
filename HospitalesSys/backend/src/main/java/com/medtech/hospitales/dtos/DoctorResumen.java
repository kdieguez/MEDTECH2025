package com.medtech.hospitales.dtos;

/**
 * DTO (Data Transfer Object) que representa un resumen de la información básica de un doctor,
 * utilizado principalmente para listados donde no se requiere detalle completo.
 */
public class DoctorResumen {

    /**
     * Identificador único del doctor.
     */
    private Long id;

    /**
     * Nombre del doctor.
     */
    private String nombre;

    /**
     * Apellido del doctor.
     */
    private String apellido;

    /**
     * URL o referencia de la fotografía del doctor.
     */
    private String fotografia;

    /**
     * Constructor para inicializar un objeto {@link DoctorResumen}.
     *
     * @param id ID del doctor
     * @param nombre nombre del doctor
     * @param apellido apellido del doctor
     * @param fotografia URL o referencia de la fotografía
     */
    public DoctorResumen(Long id, String nombre, String apellido, String fotografia) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fotografia = fotografia;
    }

    /**
     * Obtiene el ID del doctor.
     *
     * @return ID del doctor
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del doctor.
     *
     * @param id ID del doctor
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del doctor.
     *
     * @return nombre del doctor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del doctor.
     *
     * @param nombre nombre del doctor
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene el apellido del doctor.
     *
     * @return apellido del doctor
     */
    public String getApellido() {
        return apellido;
    }

    /**
     * Establece el apellido del doctor.
     *
     * @param apellido apellido del doctor
     */
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    /**
     * Obtiene la URL o referencia de la fotografía del doctor.
     *
     * @return fotografía del doctor
     */
    public String getFotografia() {
        return fotografia;
    }

    /**
     * Establece la URL o referencia de la fotografía del doctor.
     *
     * @param fotografia fotografía del doctor
     */
    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }
}
