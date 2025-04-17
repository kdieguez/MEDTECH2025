package com.medtech.hospitales.models;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;

@Embeddable
public class ServicioXDoctorId implements Serializable {

    private Long idServicio;
    private Long idInfoDoctor;

    public ServicioXDoctorId() {}

    public ServicioXDoctorId(Long idServicio, Long idInfoDoctor) {
        this.idServicio = idServicio;
        this.idInfoDoctor = idInfoDoctor;
    }

    public Long getIdServicio() { return idServicio; }
    public void setIdServicio(Long idServicio) { this.idServicio = idServicio; }

    public Long getIdInfoDoctor() { return idInfoDoctor; }
    public void setIdInfoDoctor(Long idInfoDoctor) { this.idInfoDoctor = idInfoDoctor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServicioXDoctorId)) return false;
        ServicioXDoctorId that = (ServicioXDoctorId) o;
        return Objects.equals(idServicio, that.idServicio) &&
               Objects.equals(idInfoDoctor, that.idInfoDoctor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idServicio, idInfoDoctor);
    }
}
