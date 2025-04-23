package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * Entidad que representa el encabezado general de una receta médica,
 * relacionando paciente, doctor y fecha de emisión de la receta.
 */
@Entity
@Table(name = "RECETA_MEDICA_HEADER")
public class RecetaMedicaHeader {

    /**
     * Identificador único del encabezado de receta médica.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA")
    private Long id;

    /**
     * Código único de la receta médica.
     */
    @Column(name = "CODIGO_RECETA", unique = true)
    private String codigoReceta;

    /**
     * Fecha de emisión de la receta médica.
     */
    @Column(name = "FECHA_RECETA")
    private LocalDate fechaReceta;

    /**
     * Paciente al que se le emite la receta.
     */
    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE")
    private PerfilPaciente paciente;

    /**
     * Doctor que emite la receta.
     */
    @ManyToOne
    @JoinColumn(name = "ID_INFO_DOCTOR")
    private InfoDoctor infoDoctor;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoReceta() { return codigoReceta; }
    public void setCodigoReceta(String codigoReceta) { this.codigoReceta = codigoReceta; }

    public LocalDate getFechaReceta() { return fechaReceta; }
    public void setFechaReceta(LocalDate fechaReceta) { this.fechaReceta = fechaReceta; }

    public PerfilPaciente getPaciente() { return paciente; }
    public void setPaciente(PerfilPaciente paciente) { this.paciente = paciente; }

    public InfoDoctor getInfoDoctor() { return infoDoctor; }
    public void setInfoDoctor(InfoDoctor infoDoctor) { this.infoDoctor = infoDoctor; }
}
