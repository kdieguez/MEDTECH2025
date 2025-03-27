package com.medtech.hospitales.models;

import jakarta.persistence.*;

@Entity
@Table(name = "HeaderFooter")
public class HeaderFooter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HF")
    private Long idHf;

    @Column(name = "TITULO", nullable = false, length = 100)
    private String titulo;

    @Column(name = "CONTENIDO", nullable = false, length = 255)
    private String contenido;

    // Getters y setters
    public Long getIdHf() {
        return idHf;
    }

    public void setIdHf(Long idHf) {
        this.idHf = idHf;
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
}
