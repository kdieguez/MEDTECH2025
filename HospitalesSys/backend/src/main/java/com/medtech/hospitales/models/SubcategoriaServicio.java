package com.medtech.hospitales.models;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "SUBCATEGORIAS_SERVICIO")
public class SubcategoriaServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SUBCATEGORIA")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_SERVICIO")
    @JsonBackReference
    private ServicioHospitalario servicio;

    @Column(name = "NOMBRE_SUBCATEGORIA")
    private String nombre;

    @Column(name = "DESCRIPCION")
    private String descripcion;

    @Column(name = "PRECIO")
    private Double precio;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ServicioHospitalario getServicio() { return servicio; }
    public void setServicio(ServicioHospitalario servicio) { this.servicio = servicio; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
