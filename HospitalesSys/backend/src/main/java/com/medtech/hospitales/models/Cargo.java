package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "CARGO_EMPLEADO")
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARGO")
    private Integer id;

    @Column(name = "NOMBRE_CARGO")
    private String nombre;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
