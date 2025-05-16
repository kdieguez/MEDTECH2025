package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa el encabezado general de una receta médica,
 * asociando al paciente, doctor, fecha de emisión y un código único de receta.
 */
@Entity
@Table(name = "RECETA_MEDICA_HEADER")
public class RecetaMedicaHeader {

    /**
     * Identificador único del encabezado de la receta médica.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA")
    private Long id;

    /**
     * Código único asignado a la receta médica
     * con formato "HOSP-SEG-IDRECETA".
     */
    @Column(name = "CODIGO_RECETA", unique = true)
    private String codigoReceta;

    /**
     * Fecha en que se emite la receta médica.
     */
    @Column(name = "FECHA_RECETA")
    private LocalDate fechaReceta;

    /**
     * Paciente al que se le entrega la receta médica.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE", nullable = false)
    private PerfilPaciente paciente;

    /**
     * Doctor que prescribe la receta médica.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INFO_DOCTOR", nullable = false)
    private InfoDoctor infoDoctor;

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
