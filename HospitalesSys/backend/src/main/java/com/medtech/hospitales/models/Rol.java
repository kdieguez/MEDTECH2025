package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "ROLES_SISTEMA")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Integer id;

    @Column(name = "NOMBRE_ROL", nullable = false, unique = true)
    private String nombreRol;

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombreRol() { return nombreRol; }
    public void setNombreRol(String nombreRol) { this.nombreRol = nombreRol; }
}
