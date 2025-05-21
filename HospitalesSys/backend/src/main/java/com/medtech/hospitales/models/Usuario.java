package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Entidad que representa un usuario registrado en el sistema hospitalario.
 * <p>
 * Un usuario puede tener distintos roles (administrador, doctor, paciente, etc.)
 * y cuenta con información personal básica, credenciales de acceso, y estado de activación.
 * </p>
 */
@Entity
@Table(name = "USUARIOS")
public class Usuario {

    /** Identificador único del usuario. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "USUARIOS_SEQ", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    /** Nombre del usuario. */
    @Column(name = "NOMBRE", nullable = false)
    private String nombre;

    /** Apellido del usuario. */
    @Column(name = "APELLIDO", nullable = false)
    private String apellido;

    /** Nombre de usuario (único) para autenticación. */
    @Column(name = "USUARIO", nullable = false, unique = true)
    private String usuario;

    /** Correo electrónico del usuario (único). */
    @Column(name = "CORREO", nullable = false, unique = true)
    private String email;

    /** Contraseña del usuario en formato plano o hasheado. */
    @Column(name = "CONTRASENA", nullable = false)
    private String password;

    /** Identificador del rol asignado al usuario (puede ser nulo inicialmente). */
    @Column(name = "ID_ROL", nullable = true)
    private Integer idRol;

    /** Estado de habilitación: 0 (desactivado), 1 (activo). */
    @Column(name = "ACTIVO", nullable = false)
    private Integer habilitado = 0;

    /** Fecha de creación de la cuenta. */
    @Column(name = "FECHACREACION")
    private Timestamp fechaCreacion;

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public Integer getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Integer habilitado) {
        this.habilitado = habilitado;
    }

    public Timestamp getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Timestamp fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }


    public void setCorreo(String correo) {
        this.email = correo;
    }

    public void setContrasena(String contrasena) {
        this.password = contrasena;
    }

    public void setEstadoCuenta(int estado) {
    }

    public void setRol(Rol rol) {
    }

    public Integer getRol() {
        return idRol;
    }

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
}
