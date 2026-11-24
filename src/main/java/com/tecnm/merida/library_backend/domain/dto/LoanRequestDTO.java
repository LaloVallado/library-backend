package com.tecnm.merida.library_backend.domain.dto;

import java.time.LocalDate;
import java.util.List;

// DTO complejo del POST /loans: cabecera (Maestro) + lista de libros (Detalle) en un solo JSON
public class LoanRequestDTO {

    private Integer idSocio;
    private LocalDate fechaDevolucion;
    private String comentario;
    private List<LoanItemRequestDTO> librosPrestados;

    public Integer getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Integer idSocio) {
        this.idSocio = idSocio;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public List<LoanItemRequestDTO> getLibrosPrestados() {
        return librosPrestados;
    }

    public void setLibrosPrestados(List<LoanItemRequestDTO> librosPrestados) {
        this.librosPrestados = librosPrestados;
    }
}