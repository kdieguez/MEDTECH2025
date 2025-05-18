package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Entidad que representa un usuario registrado en el sistema hospitalario.
 */
@Entity
@Table(name = "USUARIOS")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "USUARIOS_SEQ", allocationSize = 1)
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
    private Timestamp fechaCreacion;

    // Getters y Setters
    public Long getId() { return id; }
    //public void setId(Long id) { this.id = id; }

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

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public void setCorreo(String correo) {
        this.email = correo;
    }

    public void setContrasena(String contrasena) {
        this.password = contrasena;
    }

    public void setEstadoCuenta(int estado) {}  

    public void setRol(Rol rol) {}




    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", idRol=" + idRol +
                ", habilitado=" + habilitado +
                '}';
    }
    public Integer getRol() {
    return idRol;
}

public String getContrasena() {
    return password;
}

}
