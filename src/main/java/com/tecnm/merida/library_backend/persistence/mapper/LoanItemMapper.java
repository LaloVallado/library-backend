package com.tecnm.merida.library_backend.persistence.mapper;

import com.tecnm.merida.library_backend.domain.LoanItem;
import com.tecnm.merida.library_backend.persistence.entity.PrestamoLibro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface LoanItemMapper {

    @Mappings({
            @Mapping(source = "idLibro", target = "bookId"),
            @Mapping(source = "libro.titulo", target = "bookTitle"),
            @Mapping(source = "cantidad", target = "quantity")
    })
    LoanItem toLoanItem(PrestamoLibro prestamoLibro);

    // idDetalle lo genera la BD y prestamo se asigna en PrestamoRepository antes de guardar
    @Mappings({
            @Mapping(source = "bookId", target = "idLibro"),
            @Mapping(source = "quantity", target = "cantidad"),
            @Mapping(target = "idDetalle", ignore = true),
            @Mapping(target = "prestamo", ignore = true),
            @Mapping(target = "libro", ignore = true)
    })
    PrestamoLibro toPrestamoLibro(LoanItem loanItem);
}