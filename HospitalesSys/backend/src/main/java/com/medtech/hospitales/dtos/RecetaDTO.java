package com.medtech.hospitales.dtos;

import java.time.LocalDate;

/**
 * DTO (Data Transfer Object) que representa la información general de una receta médica,
 * incluyendo datos del paciente, doctor, especialidad y fecha de cita.
 */
public class RecetaDTO {

    /**
     * Fecha en que se realizó la cita médica asociada a la receta.
     */
    private LocalDate fechaCita;

    /**
     * Código único que identifica la receta médica.
     */
    private String codigoReceta;

    /**
     * Nombre completo del paciente al que se le emitió la receta.
     */
    private String nombrePaciente;

    /**
     * Nombre completo del doctor que emitió la receta.
     */
    private String nombreDoctor;

    /**
     * Número de colegiado del doctor que emitió la receta.
     */
    private String numColegiado;

    /**
     * Especialidad médica del doctor que emitió la receta.
     */
    private String especialidad;

    /**
     * Obtiene la fecha de la cita médica.
     *
     * @return fecha de la cita
     */
    public LocalDate getFechaCita() {
        return fechaCita;
    }

    /**
     * Establece la fecha de la cita médica.
     *
     * @param fechaCita fecha de la cita
     */
    public void setFechaCita(LocalDate fechaCita) {
        this.fechaCita = fechaCita;
    }

    /**
     * Obtiene el código de la receta médica.
     *
     * @return código de la receta
     */
    public String getCodigoReceta() {
        return codigoReceta;
    }

    /**
     * Establece el código de la receta médica.
     *
     * @param codigoReceta código de la receta
     */
    public void setCodigoReceta(String codigoReceta) {
        this.codigoReceta = codigoReceta;
    }

    /**
     * Obtiene el nombre completo del paciente.
     *
     * @return nombre del paciente
     */
    public String getNombrePaciente() {
        return nombrePaciente;
    }

    /**
     * Establece el nombre completo del paciente.
     *
     * @param nombrePaciente nombre del paciente
     */
    public void setNombrePaciente(String nombrePaciente) {
        this.nombrePaciente = nombrePaciente;
    }

    /**
     * Obtiene el nombre completo del doctor.
     *
     * @return nombre del doctor
     */
    public String getNombreDoctor() {
        return nombreDoctor;
    }

    /**
     * Establece el nombre completo del doctor.
     *
     * @param nombreDoctor nombre del doctor
     */
    public void setNombreDoctor(String nombreDoctor) {
        this.nombreDoctor = nombreDoctor;
    }

    /**
     * Obtiene el número de colegiado del doctor.
     *
     * @return número de colegiado
     */
    public String getNumColegiado() {
        return numColegiado;
    }

    /**
     * Establece el número de colegiado del doctor.
     *
     * @param numColegiado número de colegiado
     */
    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }

    /**
     * Obtiene la especialidad médica del doctor.
     *
     * @return especialidad del doctor
     */
    public String getEspecialidad() {
        return especialidad;
    }

    /**
     * Establece la especialidad médica del doctor.
     *
     * @param especialidad especialidad del doctor
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }
}
