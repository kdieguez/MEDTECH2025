package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa la relación entre un servicio hospitalario y un doctor.
 * Utiliza una clave compuesta {@link ServicioXDoctorId}.
 */
@Entity
@Table(name = "SERVICIO_X_DOCTOR")
public class ServicioXDoctor {

    /**
     * Clave primaria compuesta para la relación servicio-doctor.
     */
    @EmbeddedId
    private ServicioXDoctorId id;

    /**
     * Servicio hospitalario asignado.
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idServicio")
    @JoinColumn(name = "ID_SERVICIO")
    private ServicioHospitalario servicio;

    /**
     * Doctor que presta el servicio.
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
     * Constructor que asocia un servicio con un doctor.
     *
     * @param servicio servicio hospitalario
     * @param doctor doctor asociado
     */
    public ServicioXDoctor(ServicioHospitalario servicio, InfoDoctor doctor) {
        this.servicio = servicio;
        this.doctor = doctor;
        this.id = new ServicioXDoctorId(servicio.getId(), doctor.getId());
    }

    // Getters y Setters
    public ServicioXDoctorId getId() { return id; }
    public void setId(ServicioXDoctorId id) { this.id = id; }

    public ServicioHospitalario getServicio() { return servicio; }
    public void setServicio(ServicioHospitalario servicio) { this.servicio = servicio; }

    public InfoDoctor getDoctor() { return doctor; }
    public void setDoctor(InfoDoctor doctor) { this.doctor = doctor; }
}
