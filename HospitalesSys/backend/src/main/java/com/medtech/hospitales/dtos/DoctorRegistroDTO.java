package com.medtech.hospitales.dtos;

import java.util.List;

/**
 * DTO (Data Transfer Object) utilizado para registrar un nuevo perfil de doctor en el sistema.
 * <p>
 * Contiene información básica del doctor como número de colegiado, fotografía,
 * usuario asociado, teléfonos de contacto y especialidades médicas.
 * </p>
 */
public class DoctorRegistroDTO {

    /** URL o referencia a la fotografía del doctor. */
    private String fotografia;

    /** Número de colegiado profesional del doctor. */
    private String numColegiado;

    /** ID del usuario previamente creado al que se vincula este perfil de doctor. */
    private Long idUsuario;

    /** Lista de números telefónicos del doctor. */
    private List<String> telefonos;

    /** Lista de especialidades médicas con detalles de cada una. */
    private List<EspecialidadDTO> especialidades;

    /**
     * Obtiene la URL de la fotografía del doctor.
     *
     * @return fotografía del doctor
     */
    public String getFotografia() {
        return fotografia;
    }

    /**
     * Establece la URL de la fotografía del doctor.
     *
     * @param fotografia fotografía del doctor
     */
    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
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
     * Obtiene el ID del usuario al que está vinculado el doctor.
     *
     * @return ID del usuario
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Establece el ID del usuario al que está vinculado el doctor.
     *
     * @param idUsuario ID del usuario
     */
    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    /**
     * Obtiene la lista de teléfonos asociados al doctor.
     *
     * @return lista de teléfonos
     */
    public List<String> getTelefonos() {
        return telefonos;
    }

    /**
     * Establece la lista de teléfonos del doctor.
     *
     * @param telefonos lista de teléfonos
     */
    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    /**
     * Obtiene la lista de especialidades médicas asociadas al doctor.
     *
     * @return lista de especialidades
     */
    public List<EspecialidadDTO> getEspecialidades() {
        return especialidades;
    }

    /**
     * Establece la lista de especialidades médicas del doctor.
     *
     * @param especialidades lista de especialidades
     */
    public void setEspecialidades(List<EspecialidadDTO> especialidades) {
        this.especialidades = especialidades;
    }
}
