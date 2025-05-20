package com.medtech.hospitales.models;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SECCIONES_PENDIENTES")
public class SeccionPendiente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SECCION_PENDIENTE")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PAGINA", nullable = false)
    private Pagina pagina;

    @Column(name = "TITULO")
    private String titulo;

    @Lob
    @Column(name = "CONTENIDO")
    private String contenido;

    @Column(name = "IMAGEN_URL", length = 500)
    private String imagenUrl;

    @Column(name = "ORDEN")
    private Integer orden;

    @Column(name = "TIPO", length = 500)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_SOLICITANTE", nullable = false)
    private Usuario usuarioSolicitante;

    @Column(name = "ESTADO_APROBACION", length = 20)
    private String estadoAprobacion = "PENDIENTE";

    @Column(name = "FECHA_SOLICITUD")
    private Timestamp fechaSolicitud;

    @Column(name = "FECHA_APROBACION")
    private Timestamp fechaAprobacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO_APROBADOR")
    private Usuario usuarioAprobador;

    @Column(name = "COMENTARIO_RECHAZO", length = 500)
    private String comentarioRechazo;

    // Getters y Setters

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
}