package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

/**
 * Entidad que representa una sección pendiente de aprobación dentro de una página informativa.
 * <p>
 * Se utiliza como parte del sistema de moderación de contenido, permitiendo a los usuarios proponer
 * cambios que serán aprobados o rechazados por un usuario autorizado.
 * </p>
 */
@Entity
@Table(name = "SECCIONES_PENDIENTES")
public class SeccionPendiente {

    /** Identificador único de la sección pendiente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SECCION_PENDIENTE")
    private Long id;

    /** Página a la que se asocia esta sección pendiente. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PAGINA", nullable = false)
    private Pagina pagina;

    /** Título propuesto para la sección. */
    @Column(name = "TITULO")
    private String titulo;

    /** Contenido textual propuesto para la sección. */
    @Lob
    @Column(name = "CONTENIDO")
    private String contenido;

    /** URL de la imagen asociada a la sección. */
    @Column(name = "IMAGEN_URL", length = 500)
    private String imagenUrl;

    /** Orden sugerido para esta sección dentro de la página. */
    @Column(name = "ORDEN")
    private Integer orden;

    /** Tipo de operación: "CREAR", "MODIFICAR" o "ELIMINAR". */
    @Column(name = "TIPO", length = 500)
    private String tipo;

    /** Usuario que propuso el cambio. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_SOLICITANTE", nullable = false)
    private Usuario usuarioSolicitante;

    /** Estado de aprobación de la propuesta: "PENDIENTE", "APROBADO", "RECHAZADO". */
    @Column(name = "ESTADO_APROBACION", length = 20)
    private String estadoAprobacion = "PENDIENTE";

    /** Fecha en la que se realizó la solicitud. */
    @Column(name = "FECHA_SOLICITUD")
    private Timestamp fechaSolicitud;

    /** Fecha en la que se aprobó o rechazó la solicitud. */
    @Column(name = "FECHA_APROBACION")
    private Timestamp fechaAprobacion;

    /** Usuario que aprobó o rechazó el cambio. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_APROBADOR")
    private Usuario usuarioAprobador;

    /** Comentario opcional que explica la razón del rechazo. */
    @Column(name = "COMENTARIO_RECHAZO", length = 500)
    private String comentarioRechazo;

    /** ID de la sección original que será modificada o eliminada (si aplica). */
    @Column(name = "ID_SECCION_ORIGINAL")
    private Long idSeccionOriginal;

    // --- Getters y Setters ---

    public Long getId() {
        return id;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuarioSolicitante() {
        return usuarioSolicitante;
    }

    public void setUsuarioSolicitante(Usuario usuarioSolicitante) {
        this.usuarioSolicitante = usuarioSolicitante;
    }

    public String getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(String estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }

    public Timestamp getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(Timestamp fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public Timestamp getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Timestamp fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Usuario getUsuarioAprobador() {
        return usuarioAprobador;
    }

    public void setUsuarioAprobador(Usuario usuarioAprobador) {
        this.usuarioAprobador = usuarioAprobador;
    }

    public String getComentarioRechazo() {
        return comentarioRechazo;
    }

    public void setComentarioRechazo(String comentarioRechazo) {
        this.comentarioRechazo = comentarioRechazo;
    }

    public Long getIdSeccionOriginal() {
        return idSeccionOriginal;
    }

    public void setIdSeccionOriginal(Long idSeccionOriginal) {
        this.idSeccionOriginal = idSeccionOriginal;
    }
}
