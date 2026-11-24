package com.tecnm.merida.library_backend.domain.dto;

// Cada libro que viene en el JSON del prestamo (Detalle)
public class LoanItemRequestDTO {

    private Integer idLibro;
    private Integer cantidad;

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
}