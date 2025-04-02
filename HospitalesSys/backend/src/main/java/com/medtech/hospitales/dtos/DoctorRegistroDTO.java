package com.medtech.hospitales.dtos;

import java.util.List;

public class DoctorRegistroDTO {
    private String fotografia;
    private String numColegiado;
    private Long idUsuario;
    private List<String> telefonos;
    private List<EspecialidadDTO> especialidades;

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }

    public String getNumColegiado() {
        return numColegiado;
    }

    public void setNumColegiado(String numColegiado) {
        this.numColegiado = numColegiado;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public List<String> getTelefonos() {
        return telefonos;
    }

    public void setTelefonos(List<String> telefonos) {
        this.telefonos = telefonos;
    }

    public List<EspecialidadDTO> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(List<EspecialidadDTO> especialidades) {
        this.especialidades = especialidades;
    }
}
