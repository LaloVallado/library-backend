package com.tecnm.merida.library_backend.domain.dto;

import java.time.LocalDate;

// Datos de la cabecera que se pueden cambiar con PUT /loans/{id}
public class LoanUpdateDTO {

    private LocalDate fechaDevolucion;
    private String estado;
    private String comentario;

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(LocalDate fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}