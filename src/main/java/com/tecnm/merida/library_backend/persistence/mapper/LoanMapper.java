package com.tecnm.merida.library_backend.persistence.mapper;

import com.tecnm.merida.library_backend.domain.Loan;
import com.tecnm.merida.library_backend.persistence.entity.Prestamo;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

// Convierte la cabecera Prestamo <-> Loan, y su lista de detalle con LoanItemMapper
@Mapper(componentModel = "spring", uses = {LoanItemMapper.class})
public interface LoanMapper {

    @Mappings({
            @Mapping(source = "idPrestamo", target = "loanId"),
            @Mapping(source = "idSocio", target = "memberId"),
            @Mapping(source = "fechaPrestamo", target = "loanDate"),
            @Mapping(source = "fechaDevolucion", target = "returnDate"),
            @Mapping(source = "estado", target = "state"),
            @Mapping(source = "comentario", target = "comment"),
            @Mapping(source = "libros", target = "items")
    })
    Loan toLoan(Prestamo prestamo);

    List<Loan> toLoans(List<Prestamo> prestamos);

    @InheritInverseConfiguration
    @Mapping(target = "socio", ignore = true)
    Prestamo toPrestamo(Loan loan);
}