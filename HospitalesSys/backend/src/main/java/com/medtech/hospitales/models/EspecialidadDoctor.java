package com.medtech.hospitales.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Entidad que representa la relación entre un doctor y su especialidad médica,
 * incluyendo detalles de graduación y fotografía del título.
 */
@Entity
@Table(name = "ESPECIALIDAD_X_DOCTOR")
public class EspecialidadDoctor {

    /**
     * Identificador único de la relación especialidad-doctor.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESPXDOC")
    private Long id;

    /**
     * Doctor asociado a la especialidad.
     */
    @ManyToOne
    @JoinColumn(name = "ID_INFO_DOCTOR")
    @JsonBackReference
    private InfoDoctor infoDoctor;

    /**
     * Especialidad médica asociada al doctor.
     */
    @ManyToOne
    @JoinColumn(name = "ID_ESPECIALIDAD")
    private Especialidad especialidad;

    /**
     * Universidad donde el doctor obtuvo su especialidad.
     */
    @Column(name = "UNIVERSIDADGRADUACION")
    private String universidad;

    /**
     * Fecha de graduación de la especialidad.
     */
    @Column(name = "FECHAGRADUACION")
    private LocalDate fechaGraduacion;

    /**
     * URL o referencia de la fotografía del título de graduación.
     */
    @Column(name = "FOTOGRAFIATITULO")
    private String fotografiaTitulo;

    /**
     * Constructor vacío requerido por JPA.
     */
    public EspecialidadDoctor() {}

    /**
     * Obtiene el ID de la relación especialidad-doctor.
     *
     * @return ID de la relación
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la relación especialidad-doctor.
     *
     * @param id ID de la relación
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el doctor asociado a la especialidad.
     *
     * @return doctor
     */
    public InfoDoctor getInfoDoctor() {
        return infoDoctor;
    }

    /**
     * Establece el doctor asociado a la especialidad.
     *
     * @param infoDoctor doctor
     */
    public void setInfoDoctor(InfoDoctor infoDoctor) {
        this.infoDoctor = infoDoctor;
    }

    /**
     * Obtiene la especialidad médica asociada.
     *
     * @return especialidad médica
     */
    public Especialidad getEspecialidad() {
        return especialidad;
    }

    /**
     * Establece la especialidad médica asociada.
     *
     * @param especialidad especialidad médica
     */
    public void setEspecialidad(Especialidad especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Obtiene la universidad de graduación.
     *
     * @return universidad de graduación
     */
    public String getUniversidad() {
        return universidad;
    }

    /**
     * Establece la universidad de graduación.
     *
     * @param universidad universidad de graduación
     */
    public void setUniversidad(String universidad) {
        this.universidad = universidad;
    }

    /**
     * Obtiene la fecha de graduación de la especialidad.
     *
     * @return fecha de graduación
     */
    public LocalDate getFechaGraduacion() {
        return fechaGraduacion;
    }

    /**
     * Establece la fecha de graduación de la especialidad.
     *
     * @param fechaGraduacion fecha de graduación
     */
    public void setFechaGraduacion(LocalDate fechaGraduacion) {
        this.fechaGraduacion = fechaGraduacion;
    }

    /**
     * Obtiene la fotografía del título universitario.
     *
     * @return fotografía del título
     */
    public String getFotografiaTitulo() {
        return fotografiaTitulo;
    }

    /**
     * Establece la fotografía del título universitario.
     *
     * @param fotografiaTitulo fotografía del título
     */
    public void setFotografiaTitulo(String fotografiaTitulo) {
        this.fotografiaTitulo = fotografiaTitulo;
    }
}
