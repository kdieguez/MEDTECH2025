package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ESPECIALIDADES")
public class Especialidad {
    @Id
    @Column(name = "ID_ESPECIALIDAD")
    private Long id;

    @Column(name = "NOMBRE_ESPECIALIDAD")
    private String nombre;

    public Especialidad() {}
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}

