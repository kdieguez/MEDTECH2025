package com.medtech.hospitales.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;

@Entity
@Table(name = "ESPECIALIDAD_X_DOCTOR")
public class EspecialidadDoctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESPXDOC")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_INFO_DOCTOR")
    @JsonBackReference
    private InfoDoctor infoDoctor;

    @ManyToOne
    @JoinColumn(name = "ID_ESPECIALIDAD")
    private Especialidad especialidad;

    @Column(name = "UNIVERSIDADGRADUACION")
    private String universidad;

    @Column(name = "FECHAGRADUACION")
    private LocalDate fechaGraduacion;

    @Column(name = "FOTOGRAFIATITULO")
    private String fotografiaTitulo;

    public EspecialidadDoctor() {}
    
    public Long getId() {
    return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InfoDoctor getInfoDoctor() {
        return infoDoctor;
    }

    public void setInfoDoctor(InfoDoctor infoDoctor) {
        this.infoDoctor = infoDoctor;
    }

    public Especialidad getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    public String getUniversidad() {
        return universidad;
    }

    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    public LocalDate getFechaGraduacion() {
        return fechaGraduacion;
    }

    public void setFechaGraduacion(LocalDate fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    public String getFotografiaTitulo() {
        return fotografiaTitulo;
    }

    public void setFotografiaTitulo(String fotografiaTitulo) {
        this.fotografiaTitulo = fotografiaTitulo;
    }

}
