package com.medtech.hospitales.models;

import jakarta.persistence.*;

/**
 * Entidad que representa un registro para gestionar el contenido dinámico
 * del header o footer del sistema hospitalario.
 */
@Entity
@Table(name = "HEADERFOOTER")
public class HeaderFooter {

    /**
     * Identificador único del registro de header o footer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HF")
    private Long id;

    /**
     * Título asociado (por ejemplo, nombre del sistema o sección).
     */
    @Column(name = "TITULO")
    private String titulo;

    /**
     * Contenido dinámico del header o footer (por ejemplo, teléfonos, correos, textos).
     */
    @Column(name = "CONTENIDO")
    private String contenido;

    /**
     * Constructor vacío requerido por JPA.
     */
    public HeaderFooter() {}

    /**
     * Obtiene el ID del registro.
     *
     * @return ID del header/footer
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el ID del registro.
     *
     * @param id ID del header/footer
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el título asociado.
     *
     * @return título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Establece el título asociado.
     *
     * @param titulo título
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtiene el contenido dinámico.
     *
     * @return contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Establece el contenido dinámico.
     *
     * @param contenido contenido
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
}
