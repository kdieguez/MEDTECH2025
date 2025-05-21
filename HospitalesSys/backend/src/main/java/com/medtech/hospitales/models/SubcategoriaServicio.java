package com.medtech.hospitales.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Entidad que representa una subcategoría de un servicio hospitalario.
 * <p>
 * Las subcategorías permiten clasificar y especificar los diferentes tipos de atención
 * ofrecidos dentro de un servicio médico general. También contienen su propio precio.
 * </p>
 */
@Entity
@Table(name = "SUBCATEGORIAS_SERVICIO")
public class SubcategoriaServicio {

    /** Identificador único de la subcategoría. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUBCATEGORIA")
    private Long id;

    /** Servicio hospitalario al que pertenece esta subcategoría. */
    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO")
    @JsonBackReference
    private ServicioHospitalario servicio;

    /** Nombre de la subcategoría (por ejemplo, "Tórax", "Abdomen"). */
    @Column(name = "NOMBRE_SUBCATEGORIA")
    private String nombre;

    /** Descripción detallada de la subcategoría. */
    @Column(name = "DESCRIPCION")
    private String descripcion;

    /** Precio específico asociado a esta subcategoría del servicio. */
    @Column(name = "PRECIO")
    private Double precio;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServicioHospitalario getServicio() {
        return servicio;
    }

    public void setServicio(ServicioHospitalario servicio) {
        this.servicio = servicio;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
