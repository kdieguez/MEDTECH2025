package com.medtech.hospitales.models;

import java.io.Serializable;
import java.util.Objects;
import jakarta.persistence.Embeddable;

/**
 * Clase embebible que representa la clave primaria compuesta para la entidad {@link ServicioXDoctor}.
 * <p>
 * Esta clave está compuesta por el ID del servicio hospitalario y el ID del doctor,
 * lo cual permite representar una relación muchos-a-muchos con atributos adicionales.
 * </p>
 */
@Embeddable
public class ServicioXDoctorId implements Serializable {

    /** Identificador del servicio hospitalario. */
    private Long idServicio;

    /** Identificador del doctor (InfoDoctor). */
    private Long idInfoDoctor;

    /** Constructor vacío requerido por JPA. */
    public ServicioXDoctorId() {}

    /**
     * Constructor con todos los campos.
     *
     * @param idServicio ID del servicio hospitalario
     * @param idInfoDoctor ID del doctor
     */
    public ServicioXDoctorId(Long idServicio, Long idInfoDoctor) {
        this.idServicio = idServicio;
        this.idInfoDoctor = idInfoDoctor;
    }


    public Long getIdServicio() {
        return idServicio;
    }

    public void setIdServicio(Long idServicio) {
        this.idServicio = idServicio;
    }

    public Long getIdInfoDoctor() {
        return idInfoDoctor;
    }

    public void setIdInfoDoctor(Long idInfoDoctor) {
        this.idInfoDoctor = idInfoDoctor;
    }

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
