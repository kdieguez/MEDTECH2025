package com.medtech.hospitales.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un servicio hospitalario ofrecido por el hospital.
 * <p>
 * Cada servicio puede tener múltiples subcategorías (por ejemplo, tipos de exámenes o procedimientos)
 * y puede estar asociado a uno o varios doctores a través de la tabla de relación {@code SERVICIO_X_DOCTOR}.
 * </p>
 */
@Entity
@Table(name = "SERVICIOS_HOSPITALARIOS")
public class ServicioHospitalario {

    /** Identificador único del servicio hospitalario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SERVICIO")
    private Long id;

    /** Nombre del servicio hospitalario (ej. "Radiología", "Cardiología"). */
    @Column(name = "NOMBRE_SERVICIO")
    private String nombre;

    /** Descripción del servicio hospitalario. */
    @Column(name = "DESCRIPCION")
    private String descripcion;

    /**
     * Lista de subcategorías asociadas al servicio (por ejemplo, "Rayos X de Tórax").
     * Relación uno a muchos con {@link SubcategoriaServicio}.
     */
    @OneToMany(mappedBy = "servicio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<SubcategoriaServicio> subcategorias = new ArrayList<>();

    /**
     * Lista de doctores que están habilitados para brindar este servicio.
     * Relación muchos a muchos con {@link InfoDoctor} mediante la tabla intermedia {@code SERVICIO_X_DOCTOR}.
     */
    @ManyToMany
    @JoinTable(
        name = "SERVICIO_X_DOCTOR",
        joinColumns = @JoinColumn(name = "ID_SERVICIO"),
        inverseJoinColumns = @JoinColumn(name = "ID_INFO_DOCTOR")
    )
    private List<InfoDoctor> doctores = new ArrayList<>();

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<SubcategoriaServicio> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<SubcategoriaServicio> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public List<InfoDoctor> getDoctores() {
        return doctores;
    }

    public void setDoctores(List<InfoDoctor> doctores) {
        this.doctores = doctores;
    }
}
