package com.medtech.hospitales.models;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Entidad que representa la relación entre un doctor y una especialidad médica que posee.
 * <p>
 * Incluye detalles como la universidad donde se graduó, la fecha de graduación
 * y la fotografía del título. Cada relación está vinculada a un doctor y una especialidad.
 * </p>
 */
@Entity
@Table(name = "ESPECIALIDAD_X_DOCTOR")
public class EspecialidadDoctor {

    /** Identificador único de la relación especialidad-doctor. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ESPXDOC")
    private Long id;

    /** Doctor al que pertenece esta especialidad. */
    @ManyToOne
    @JoinColumn(name = "ID_INFO_DOCTOR")
    @JsonBackReference
    private InfoDoctor infoDoctor;

    /** Especialidad médica que posee el doctor. */
    @ManyToOne
    @JoinColumn(name = "ID_ESPECIALIDAD")
    private Especialidad especialidad;

    /** Nombre de la universidad donde se obtuvo la especialidad. */
    @Column(name = "UNIVERSIDADGRADUACION")
    private String universidad;

    /** Fecha en que el doctor se graduó en dicha especialidad. */
    @Column(name = "FECHAGRADUACION")
    private LocalDate fechaGraduacion;

    /** URL o referencia de la fotografía del título universitario. */
    @Column(name = "FOTOGRAFIATITULO")
    private String fotografiaTitulo;

    /** Constructor vacío requerido por JPA. */
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
