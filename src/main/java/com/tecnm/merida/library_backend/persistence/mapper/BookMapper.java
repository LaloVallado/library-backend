package com.tecnm.merida.library_backend.persistence.mapper;

import com.tecnm.merida.library_backend.domain.Book;
import com.tecnm.merida.library_backend.persistence.entity.Libro;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {GenreMapper.class})
public interface BookMapper {

    @Mappings({
            @Mapping(source = "idLibro", target = "bookId"),
            @Mapping(source = "titulo", target = "title"),
            @Mapping(source = "autor", target = "author"),
            @Mapping(source = "isbn", target = "isbn"),
            @Mapping(source = "idGenero", target = "genreId"),
            @Mapping(source = "anioPublicacion", target = "publicationYear"),
            @Mapping(source = "copiasDisponibles", target = "availableCopies"),
            @Mapping(source = "estado", target = "active"),
            @Mapping(source = "genero", target = "genre")
    })
    Book toBook(Libro libro);

    List<Book> toBooks(List<Libro> libros);

    // El genero se guarda solo con genreId; el objeto genero es de solo lectura
    @InheritInverseConfiguration
    @Mapping(target = "genero", ignore = true)
    Libro toLibro(Book book);
}