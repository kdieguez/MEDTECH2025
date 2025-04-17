package com.medtech.hospitales.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class CitaRegistroDTO {
    private Long idPaciente;
    private Long idDoctor;
    private Long idSubcategoria;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") 
    private LocalDateTime fechaHora;

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public Long getIdDoctor() { return idDoctor; }
    public void setIdDoctor(Long idDoctor) { this.idDoctor = idDoctor; }

    public Long getIdSubcategoria() { return idSubcategoria; }
    public void setIdSubcategoria(Long idSubcategoria) { this.idSubcategoria = idSubcategoria; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }
}
