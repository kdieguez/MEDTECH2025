package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un registro para gestionar contenido dinámico
 * del encabezado (header) o pie de página (footer) en el sistema hospitalario.
 * <p>
 * Esta entidad permite personalizar secciones como el nombre del sistema, información de contacto,
 * o cualquier otro texto dinámico mostrado en la interfaz.
 * </p>
 */
@Entity
@Table(name = "HEADERFOOTER")
public class HeaderFooter {

    /** Identificador único del registro de header o footer. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HF")
    private Long id;

    /** Título asociado al contenido. */
    @Column(name = "TITULO")
    private String titulo;

    /** Contenido dinámico. */
    @Column(name = "CONTENIDO")
    private String contenido;

    /** Constructor vacío requerido por JPA. */
    public HeaderFooter() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
