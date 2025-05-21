package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa el encabezado general de una receta médica en el sistema hospitalario.
 * <p>
 * Incluye datos esenciales como el código único de receta, la fecha de emisión,
 * el paciente al que se le entrega y el doctor que la prescribe.
 * </p>
 */
@Entity
@Table(name = "RECETA_MEDICA_HEADER")
public class RecetaMedicaHeader {

    /** Identificador único del encabezado de receta médica. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA")
    private Long id;

    /**
     * Código único asignado a la receta médica.
     * <p>Ejemplo de formato: {@code HOSP-SEG-123}</p>
     */
    @Column(name = "CODIGO_RECETA", unique = true)
    private String codigoReceta;

    /** Fecha en la que se emite la receta médica. */
    @Column(name = "FECHA_RECETA")
    private LocalDate fechaReceta;

    /** Paciente al que se le entrega la receta médica. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE", nullable = false)
    private PerfilPaciente paciente;

    /** Doctor que prescribe la receta médica. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INFO_DOCTOR", nullable = false)
    private InfoDoctor infoDoctor;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigoReceta() {
        return codigoReceta;
    }

    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    public LocalDate getFechaReceta() {
        return fechaReceta;
    }

    public void setFechaReceta(LocalDate fechaReceta) {
        this.fechaReceta = fechaReceta;
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
}
