package com.tecnm.merida.library_backend.persistence.mapper;

import com.tecnm.merida.library_backend.domain.Genre;
import com.tecnm.merida.library_backend.persistence.entity.Genero;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GenreMapper {

    @Mappings({
            @Mapping(source = "idGenero", target = "genreId"),
            @Mapping(source = "nombre", target = "name"),
            @Mapping(source = "estado", target = "active")
    })
    Genre toGenre(Genero genero);

    List<Genre> toGenres(List<Genero> generos);

    @InheritInverseConfiguration
    Genero toGenero(Genre genre);
}