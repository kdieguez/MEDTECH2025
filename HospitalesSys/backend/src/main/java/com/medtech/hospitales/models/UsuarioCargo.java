package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa la asignación de un cargo a un usuario en el sistema hospitalario.
 */
@Entity
@Table(name = "USUARIO_CARGO")
public class UsuarioCargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO_CARGO")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "ID_CARGO")
    private Cargo cargo;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public Cargo getCargo() { return cargo; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
}
