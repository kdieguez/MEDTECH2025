package com.medtech.hospitales.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Entidad que representa una sección de contenido dentro de una página informativa del sistema hospitalario.
 * <p>
 * Cada sección puede tener un título, contenido textual, una imagen asociada y un orden específico
 * dentro de la página.
 * </p>
 */
@Entity
@Table(name = "SECCIONES_PAGINA")
public class SeccionPagina {

    /** Identificador único de la sección. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SECCION")
    private Long id;

    /** Página a la que pertenece esta sección. */
    @ManyToOne
    @JoinColumn(name = "ID_PAGINA", nullable = false)
    @JsonIgnore
    private Pagina pagina;

    /** Título de la sección (por ejemplo, "Misión", "Visión"). */
    @Column(name = "TITULO")
    private String titulo;

    /** Contenido en formato texto o HTML enriquecido. */
    @Lob
    @Column(name = "CONTENIDO")
    private String contenido;

    /** URL de la imagen asociada a esta sección (si aplica). */
    @Column(name = "IMAGEN_URL")
    private String imagenUrl;

    /** Posición de esta sección dentro de la página. */
    @Column(name = "ORDEN")
    private Integer orden;

    // --- Getters y Setters ---

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
