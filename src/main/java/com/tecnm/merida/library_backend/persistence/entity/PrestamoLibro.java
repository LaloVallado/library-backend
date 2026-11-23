package com.tecnm.merida.library_backend.persistence.entity;

import jakarta.persistence.*;

// DETALLE: cada libro que se lleva el socio en un prestamo
@Entity
@Table(name = "prestamos_libros")
public class PrestamoLibro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @Column(name = "id_libro")
    private Integer idLibro;

    private Integer cantidad;

    // Dueno de la relacion: de aqui Hibernate toma el id_prestamo al guardar en cascada
    @ManyToOne
    @JoinColumn(name = "id_prestamo")
    private Prestamo prestamo;

    // Relacion de solo lectura: el libro se guarda con idLibro
    @ManyToOne
    @JoinColumn(name = "id_libro", insertable = false, updatable = false)
    private Libro libro;

    public Integer getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Integer idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
        this.idLibro = idLibro;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Prestamo getPrestamo() {
        return prestamo;
    }

    public void setPrestamo(Prestamo prestamo) {
        this.prestamo = prestamo;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }
}