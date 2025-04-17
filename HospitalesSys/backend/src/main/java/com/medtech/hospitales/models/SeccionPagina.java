package com.medtech.hospitales.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SECCIONES_PAGINA")
public class SeccionPagina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SECCION")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ID_PAGINA", nullable = false)
    @JsonIgnore
    private Pagina pagina;

    @Column(name = "TITULO")
    private String titulo;

    @Lob
    @Column(name = "CONTENIDO")
    private String contenido;

    @Column(name = "IMAGEN_URL")
    private String imagenUrl;

    @Column(name = "ORDEN")
    private Integer orden;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pagina getPagina() {
        return pagina;
    }

    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }
}