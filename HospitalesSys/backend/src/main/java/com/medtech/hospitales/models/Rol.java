package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un rol o perfil de acceso dentro del sistema hospitalario.
 * <p>
 * Los roles definen los permisos y funcionalidades a las que puede acceder un usuario,
 * tales como "Administrador", "Doctor", "Empleado", "Paciente", etc.
 * </p>
 */
@Entity
@Table(name = "ROLES_SISTEMA")
public class Rol {

    /** Identificador único del rol dentro del sistema. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Integer id;

    /** Nombre del rol (ej. "Administrador", "Doctor", "Paciente"). */
    @Column(name = "NOMBRE_ROL", nullable = false, unique = true)
    private String nombreRol;

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setNombreRol(String nombreRol) {
        this.nombreRol = nombreRol;
    }
}
