package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una cita médica en el sistema hospitalario.
 * <p>
 * Cada cita médica está asociada a un paciente, un doctor,
 * una subcategoría de servicio y una fecha y hora específicas.
 * </p>
 */
@Entity
@Table(name = "CITAS_MEDICAS")
public class CitaMedica {

    /** Identificador único de la cita médica. */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cita_seq")
    @SequenceGenerator(name = "cita_seq", sequenceName = "CITAS_MEDICAS_SEQ", allocationSize = 1)
    @Column(name = "ID_CITA")
    private Long id;

    /** Paciente que asistirá a la cita médica. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE")
    private PerfilPaciente paciente;

    /** Doctor que atiende la cita médica. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INFO_DOCTOR")
    private InfoDoctor infoDoctor;

    /** Subcategoría del servicio médico solicitado en la cita (ej. "Electrocardiograma"). */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUBCATEGORIA")
    private SubcategoriaServicio subcategoria;

    /** Fecha y hora programada para la cita médica. */
    @Column(name = "FECHAHORA", nullable = false)
    private LocalDateTime fechaHora;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PerfilPaciente getPaciente() {
        return paciente;
    }

    public void setPaciente(PerfilPaciente paciente) {
        this.paciente = paciente;
    }

    public InfoDoctor getInfoDoctor() {
        return infoDoctor;
    }

    public void setInfoDoctor(InfoDoctor infoDoctor) {
        this.infoDoctor = infoDoctor;
    }

    public SubcategoriaServicio getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(SubcategoriaServicio subcategoria) {
        this.subcategoria = subcategoria;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
