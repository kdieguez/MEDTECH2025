package com.medtech.hospitales.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SERVICIOS_HOSPITALARIOS")
public class ServicioHospitalario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICIO")
    private Long id;

    @Column(name = "NOMBRE_SERVICIO")
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SubcategoriaServicio> subcategorias = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "SERVICIO_X_DOCTOR",
        joinColumns = @JoinColumn(name = "ID_SERVICIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_INFO_DOCTOR")
    )
    private List<InfoDoctor> doctores = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public List<SubcategoriaServicio> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<SubcategoriaServicio> subcategorias) { this.subcategorias = subcategorias; }

    public List<InfoDoctor> getDoctores() { return doctores; }
    public void setDoctores(List<InfoDoctor> doctores) { this.doctores = doctores; }
}
