package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa la asignación de un {@link Cargo} específico a un {@link Usuario}
 * dentro del sistema hospitalario.
 * <p>
 * Esta entidad permite gestionar qué cargo ocupa un usuario en particular,
 * lo cual es útil para permisos, roles internos y flujos de autorización.
 * </p>
 */
@Entity
@Table(name = "USUARIO_CARGO")
public class UsuarioCargo {

    /** Identificador único de la asignación usuario-cargo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_USUARIO_CARGO")
    private Long id;

    /** Usuario al que se le asigna el cargo. */
    @ManyToOne
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    /** Cargo que se le asigna al usuario. */
    @ManyToOne
    @JoinColumn(name = "ID_CARGO")
    private Cargo cargo;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }
}
