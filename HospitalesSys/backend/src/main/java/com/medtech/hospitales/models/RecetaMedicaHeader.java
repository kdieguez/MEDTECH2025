package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "RECETA_MEDICA_HEADER")
public class RecetaMedicaHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RECETA")
    private Long id;

    @Column(name = "CODIGO_RECETA", unique = true)
    private String codigoReceta;

    @Column(name = "FECHA_RECETA")
    private LocalDate fechaReceta;

    @ManyToOne
    @JoinColumn(name = "ID_PACIENTE")
    private PerfilPaciente paciente;

    @ManyToOne
    @JoinColumn(name = "ID_INFO_DOCTOR")
    private InfoDoctor infoDoctor;

    // Getters y setters
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
