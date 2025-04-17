package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CITAS_MEDICAS")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cita_seq")
    @SequenceGenerator(name = "cita_seq", sequenceName = "CITAS_MEDICAS_SEQ", allocationSize = 1)
    @Column(name = "ID_CITA")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE")
    private PerfilPaciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INFO_DOCTOR")
    private InfoDoctor infoDoctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUBCATEGORIA")
    private SubcategoriaServicio subcategoria;

    @Column(name = "FECHAHORA", nullable = false)
    private LocalDateTime fechaHora;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PerfilPaciente getPaciente() { return paciente; }
    public void setPaciente(PerfilPaciente paciente) { this.paciente = paciente; }

    public InfoDoctor getInfoDoctor() { return infoDoctor; }
    public void setInfoDoctor(InfoDoctor infoDoctor) { this.infoDoctor = infoDoctor; }

    public SubcategoriaServicio getSubcategoria() { return subcategoria; }
    public void setSubcategoria(SubcategoriaServicio subcategoria) { this.subcategoria = subcategoria; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
