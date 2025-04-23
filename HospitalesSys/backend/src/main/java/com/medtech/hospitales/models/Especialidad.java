package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa una especialidad médica dentro del sistema hospitalario.
 */
@Entity
@Table(name = "ESPECIALIDADES")
public class Especialidad {

    /**
     * Identificador único de la especialidad médica.
     */
    @Id
    @Column(name = "ID_ESPECIALIDAD")
    private Long id;

    /**
     * Nombre de la especialidad médica.
     */
    @Column(name = "NOMBRE_ESPECIALIDAD")
    private String nombre;

    /**
     * Constructor vacío requerido por JPA.
     */
    public Especialidad() {}

    /**
     * Obtiene el ID de la especialidad.
     *
     * @return ID de la especialidad
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la especialidad.
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
