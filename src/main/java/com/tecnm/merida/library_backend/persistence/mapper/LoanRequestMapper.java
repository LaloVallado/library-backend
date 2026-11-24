package com.tecnm.merida.library_backend.persistence.mapper;

import com.tecnm.merida.library_backend.domain.Loan;
import com.tecnm.merida.library_backend.domain.LoanItem;
import com.tecnm.merida.library_backend.domain.dto.LoanItemRequestDTO;
import com.tecnm.merida.library_backend.domain.dto.LoanRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

// Convierte el JSON anidado (DTO) en el objeto Loan con su lista de LoanItem
@Mapper(componentModel = "spring")
public interface LoanRequestMapper {

    @Mappings({
            @Mapping(source = "idSocio", target = "memberId"),
            @Mapping(source = "fechaDevolucion", target = "returnDate"),
            @Mapping(source = "comentario", target = "comment"),
            @Mapping(source = "librosPrestados", target = "items"),
            @Mapping(target = "loanId", ignore = true),
            @Mapping(target = "loanDate", ignore = true),
            @Mapping(target = "state", ignore = true)
    })
    Loan toLoan(LoanRequestDTO dto);

    @Mappings({
            @Mapping(source = "idLibro", target = "bookId"),
            @Mapping(source = "cantidad", target = "quantity"),
            @Mapping(target = "bookTitle", ignore = true)
    })
    LoanItem toLoanItem(LoanItemRequestDTO dto);
}