package com.medtech.hospitales.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Entidad que representa un número telefónico asociado a un perfil profesional de doctor.
 * <p>
 * Esta clase permite que un mismo doctor tenga múltiples teléfonos registrados
 * y se encuentra relacionada con la entidad {@link InfoDoctor}.
 * </p>
 */
@Entity
@Table(name = "TELEFONO_DOCTOR")
public class TelefonoDoctor {

    /** Identificador único del número telefónico. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TELEFONO")
    private Long id;

    /** Número de teléfono del doctor. */
    @Column(name = "TELEFONO")
    private String telefono;

    /** Doctor al que pertenece este número telefónico. */
    @ManyToOne
    @JoinColumn(name = "ID_INFO_DOCTOR")
    @JsonBackReference
    private InfoDoctor infoDoctor;

    /** Constructor vacío requerido por JPA. */
    public TelefonoDoctor() {}

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public InfoDoctor getInfoDoctor() {
        return infoDoctor;
    }

    public void setInfoDoctor(InfoDoctor infoDoctor) {
        this.infoDoctor = infoDoctor;
    }
}
