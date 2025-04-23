package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad que representa una cita médica en el sistema hospitalario.
 * 
 * Relaciona a un paciente, un doctor, una subcategoría de servicio y la fecha/hora de la cita.
 */
@Entity
@Table(name = "CITAS_MEDICAS")
public class CitaMedica {

    /**
     * Identificador único de la cita médica.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cita_seq")
    @SequenceGenerator(name = "cita_seq", sequenceName = "CITAS_MEDICAS_SEQ", allocationSize = 1)
    @Column(name = "ID_CITA")
    private Long id;

    /**
     * Paciente que asiste a la cita médica.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PACIENTE")
    private PerfilPaciente paciente;

    /**
     * Doctor que atiende la cita médica.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_INFO_DOCTOR")
    private InfoDoctor infoDoctor;

    /**
     * Subcategoría del servicio médico solicitado en la cita.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SUBCATEGORIA")
    private SubcategoriaServicio subcategoria;

    /**
     * Fecha y hora programada para la cita médica.
     */
    @Column(name = "FECHAHORA", nullable = false)
    private LocalDateTime fechaHora;

    /**
     * Obtiene el ID de la cita médica.
     *
     * @return ID de la cita
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID de la cita médica.
     *
     * @param id ID de la cita
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el paciente que asiste a la cita.
     *
     * @return paciente de la cita
     */
    public PerfilPaciente getPaciente() {
        return paciente;
    }

    /**
     * Establece el paciente que asiste a la cita.
     *
     * @param paciente paciente de la cita
     */
    public void setPaciente(PerfilPaciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Obtiene el doctor que atiende la cita.
     *
     * @return doctor de la cita
     */
    public InfoDoctor getInfoDoctor() {
        return infoDoctor;
    }

    /**
     * Establece el doctor que atiende la cita.
     *
     * @param infoDoctor doctor de la cita
     */
    public void setInfoDoctor(InfoDoctor infoDoctor) {
        this.infoDoctor = infoDoctor;
    }

    /**
     * Obtiene la subcategoría del servicio de la cita.
     *
     * @return subcategoría de la cita
     */
    public SubcategoriaServicio getSubcategoria() {
        return subcategoria;
    }

    /**
     * Establece la subcategoría del servicio de la cita.
     *
     * @param subcategoria subcategoría de la cita
     */
    public void setSubcategoria(SubcategoriaServicio subcategoria) {
        this.subcategoria = subcategoria;
    }

    /**
     * Obtiene la fecha y hora programada para la cita.
     *
     * @return fecha y hora de la cita
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Establece la fecha y hora programada para la cita.
     *
     * @param fechaHora fecha y hora de la cita
     */
    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }
}
