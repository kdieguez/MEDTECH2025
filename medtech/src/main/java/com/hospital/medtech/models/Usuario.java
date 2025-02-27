package com.hospital.medtech.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "USUARIOS")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO")
    private Long id;

    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    @Column(name = "CORREO", nullable = false, unique = true)
    private String correo;

    @Column(name = "USUARIO", nullable = false, unique = true)
    private String usuario;

    @Column(name = "CONTRASENA", nullable = false)
    private String contrasena;

    @ManyToOne
    @JoinColumn(name = "ID_ROL", nullable = false)
    private Rol rol;

    @Column(name = "ACTIVO", columnDefinition = "NUMBER(1,0) DEFAULT 1")
    private Boolean activo = true;
}
