package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa la relación entre un {@link ServicioHospitalario} y un {@link InfoDoctor}.
 * <p>
 * Esta relación indica qué doctores están autorizados o habilitados para prestar un determinado servicio médico.
 * Se implementa mediante una clave primaria compuesta definida por la clase {@link ServicioXDoctorId}.
 * </p>
 */
@Entity
@Table(name = "SERVICIO_X_DOCTOR")
public class ServicioXDoctor {

    /**
     * Clave primaria compuesta (ID_SERVICIO + ID_INFO_DOCTOR).
     */
    @EmbeddedId
    private ServicioXDoctorId id;

    /**
     * Servicio hospitalario relacionado.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idServicio")
    @JoinColumn(name = "ID_SERVICIO")
    private ServicioHospitalario servicio;

    /**
     * Doctor asignado para brindar el servicio.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idInfoDoctor")
    @JoinColumn(name = "ID_INFO_DOCTOR")
    private InfoDoctor doctor;

    /**
     * Constructor vacío requerido por JPA.
     */
    public ServicioXDoctor() {}

    /**
     * Constructor que inicializa la relación entre un servicio hospitalario y un doctor.
     *
     * @param servicio el servicio hospitalario asociado
     * @param doctor el doctor asignado al servicio
     */
    public ServicioXDoctor(ServicioHospitalario servicio, InfoDoctor doctor) {
        this.servicio = servicio;
        this.doctor = doctor;
        this.id = new ServicioXDoctorId(servicio.getId(), doctor.getId());
    }

    // --- Getters y Setters ---

    public ServicioXDoctorId getId() {
        return id;
    }

    public void setId(ServicioXDoctorId id) {
        this.id = id;
    }

    public ServicioHospitalario getServicio() {
        return servicio;
    }

    public void setServicio(ServicioHospitalario servicio) {
        this.servicio = servicio;
    }

    public InfoDoctor getDoctor() {
        return doctor;
    }

    public void setDoctor(InfoDoctor doctor) {
        this.doctor = doctor;
    }
}
