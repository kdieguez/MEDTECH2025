package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "HEADERFOOTER")
public class HeaderFooter {

    @Id
    @Column(name = "ID_HF")
    private Long id;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "CONTENIDO")
    private String contenido;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getContenido() { return contenido; }
    public void setContenido(String contenido) { this.contenido = contenido; }
}
