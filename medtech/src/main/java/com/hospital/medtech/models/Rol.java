package com.hospital.medtech.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ROLES_SISTEMA")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ROL")
    private Long id;

    @Column(name = "NOMBRE_ROL", nullable = false, unique = true)
    private String nombre;
}