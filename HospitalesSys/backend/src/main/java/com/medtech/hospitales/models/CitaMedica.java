package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "CITAS_MEDICAS", schema = "HOSPITAL_BASE")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long id;

    @Column(name = "FECHAHORA", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name = "ID_PERFIL_DOCTOR", nullable = false)
    private Long idDoctor;

    @Column(name = "ID_SUBCATEGORIA", nullable = false)
    private Long idSubcategoria;

    @Column(name = "MOTIVO", nullable = false)
    private String motivo;

    @Transient
    private String fecha;
    @Transient
    private String hora;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public Long getIdDoctor() { return idDoctor; }
    public void setIdDoctor(Long idDoctor) { this.idDoctor = idDoctor; }

    public Long getIdSubcategoria() { return idSubcategoria; }
    public void setIdSubcategoria(Long idSubcategoria) { this.idSubcategoria = idSubcategoria; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
}