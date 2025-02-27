package com.hospital.medtech.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "CITAS_MEDICAS", schema = "HOSPITAL_BASE")
public class CitasMedicas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CITA")
    private Long idCita;

    @Column(name = "ID_PACIENTE", nullable = false)
    private Long idPaciente;

    @Column(name = "ID_PERFIL_DOCTOR", nullable = false)
    private Long idPerfilDoctor;

    @Column(name = "ID_SUBCATEGORIA", nullable = false)
    private Long idSubcategoria;

    @Column(name = "FECHAHORA", nullable = false)
    private LocalDateTime fechaHora;
}