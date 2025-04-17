package com.medtech.hospitales.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
@Table(name = "PAGINAS")
public class Pagina {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pagina_seq")
    @SequenceGenerator(name = "pagina_seq", sequenceName = "PAGINAS_SEQ", allocationSize = 1)
    @Column(name = "ID_PAGINA")
    private Long id;

    @Column(name = "NOMBRE", nullable = false, unique = true)
    private String nombre;

    @JsonIgnore // 🔥🔥🔥 Esto le dice a Jackson que ignore las secciones al devolver el JSON
    @OneToMany(mappedBy = "pagina", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SeccionPagina> secciones;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<SeccionPagina> getSecciones() {
        return secciones;
    }

    public void setSecciones(List<SeccionPagina> secciones) {
        this.secciones = secciones;
    }
}