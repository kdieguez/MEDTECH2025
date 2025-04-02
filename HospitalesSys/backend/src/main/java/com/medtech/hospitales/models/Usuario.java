package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "USUARIO", nullable = false, unique = true)
    private String usuario;

    @Column(name = "CORREO", nullable = false, unique = true)
    private String email;

    @Column(name = "CONTRASENA", nullable = false)
    private String password;

    @Column(name = "ID_ROL", nullable = true)
    private Integer idRol;

    @Column(name = "ACTIVO", nullable = false)
    private Integer habilitado = 0; 

    @Column(name = "FECHACREACION")
    private LocalDateTime fechaCreacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; } 


    public Integer getHabilitado() { return habilitado; }
    public void setHabilitado(Integer habilitado) { this.habilitado = habilitado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
