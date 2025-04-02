
package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "HEADERFOOTER")
public class HeaderFooter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HF")
    private Long id;

    @Column(name = "TITULO")
    private String titulo;

    @Column(name = "CONTENIDO")
    private String contenido;

    public HeaderFooter() {}

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
