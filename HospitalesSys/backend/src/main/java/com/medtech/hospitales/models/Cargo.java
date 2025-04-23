package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un cargo o puesto de un empleado dentro del sistema hospitalario.
 */
@Entity
@Table(name = "CARGO_EMPLEADO")
public class Cargo {

    /**
     * Identificador único del cargo.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARGO")
    private Integer id;

    /**
     * Nombre del cargo asignado al empleado.
     */
    @Column(name = "NOMBRE_CARGO")
    private String nombre;

    /**
     * Obtiene el ID del cargo.
     *
     * @return ID del cargo
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID del cargo.
     *
     * @param id ID del cargo
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del cargo.
     *
     * @return nombre del cargo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del cargo.
     *
     * @param nombre nombre del cargo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
