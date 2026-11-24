package com.tecnm.merida.library_backend.persistence;

import com.tecnm.merida.library_backend.domain.Genre;
import com.tecnm.merida.library_backend.domain.repository.GenreRepository;
import com.tecnm.merida.library_backend.persistence.crud.GeneroCrudRepository;
import com.tecnm.merida.library_backend.persistence.entity.Genero;
import com.tecnm.merida.library_backend.persistence.mapper.GenreMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class GeneroRepository implements GenreRepository {

    @Autowired
    private GeneroCrudRepository generoCrudRepository;

    @Autowired
    private GenreMapper mapper;

    @Override
    public List<Genre> getAll() {
        List<Genero> generos = (List<Genero>) generoCrudRepository.findAll();
        return mapper.toGenres(generos);
    }

    @Override
    public Optional<Genre> getGenre(int genreId) {
        return generoCrudRepository.findById(genreId)
                .map(genero -> mapper.toGenre(genero));
    }

    @Override
    public Genre save(Genre genre) {
        Genero genero = mapper.toGenero(genre);
        return mapper.toGenre(generoCrudRepository.save(genero));
    }

    @Override
    public void delete(int genreId) {
        generoCrudRepository.deleteById(genreId);
    }
}