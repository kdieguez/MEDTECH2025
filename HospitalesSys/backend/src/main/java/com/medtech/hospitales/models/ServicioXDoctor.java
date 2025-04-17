package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "SERVICIO_X_DOCTOR")
public class ServicioXDoctor {

    @EmbeddedId
    private ServicioXDoctorId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idServicio")
    @JoinColumn(name = "ID_SERVICIO")
    private ServicioHospitalario servicio;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idInfoDoctor")
    @JoinColumn(name = "ID_INFO_DOCTOR")
    private InfoDoctor doctor;

    public ServicioXDoctor() {}

    public ServicioXDoctor(ServicioHospitalario servicio, InfoDoctor doctor) {
        this.servicio = servicio;
        this.doctor = doctor;
        this.id = new ServicioXDoctorId(servicio.getId(), doctor.getId());
    }

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
