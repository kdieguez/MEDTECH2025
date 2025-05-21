package com.medtech.hospitales.models;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

/**
 * Entidad que representa la información profesional de un doctor en el sistema hospitalario.
 * <p>
 * Esta clase contiene detalles como fotografía, número de colegiado, usuario asociado,
 * lista de teléfonos de contacto y especialidades médicas que el doctor posee.
 * </p>
 */
@Entity
@Table(name = "INFO_DOCTOR")
public class InfoDoctor {

    /** Identificador único de la información del doctor. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_INFO_DOCTOR")
    private Long id;

    /** URL o referencia de la fotografía del doctor. */
    @Column(name = "FOTOGRAFIA")
    private String fotografia;

    /** Número de colegiado profesional que acredita al doctor. */
    @Column(name = "NUMCOLEGIADO")
    private String numColegiado;

    /** Usuario del sistema asociado al perfil del doctor. */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    /** Lista de teléfonos de contacto asociados al doctor. */
    @OneToMany(mappedBy = "infoDoctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<TelefonoDoctor> telefonos = new ArrayList<>();

    /** Lista de especialidades médicas que posee el doctor. */
    @OneToMany(mappedBy = "infoDoctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<EspecialidadDoctor> especialidades = new ArrayList<>();

    /** Constructor vacío requerido por JPA. */
    public InfoDoctor() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String getNumColegiado() {
        return numColegiado;
    }

    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<TelefonoDoctor> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<TelefonoDoctor> telefonos) {
        this.telefonos = telefonos;
    }

    public List<EspecialidadDoctor> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<EspecialidadDoctor> especialidades) {
        this.especialidades = especialidades;
    }
}
