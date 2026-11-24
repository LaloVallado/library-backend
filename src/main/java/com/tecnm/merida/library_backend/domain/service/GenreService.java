package com.tecnm.merida.library_backend.domain.service;

import com.tecnm.merida.library_backend.domain.Genre;
import com.tecnm.merida.library_backend.domain.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getAll() {
        return genreRepository.getAll();
    }

    public Optional<Genre> getGenre(int genreId) {
        return genreRepository.getGenre(genreId);
    }

    public Genre save(Genre genre) {
        validate(genre);
        genre.setGenreId(null); // en POST el id lo genera la base de datos
        return genreRepository.save(genre);
    }

    public Optional<Genre> update(int genreId, Genre genre) {
        validate(genre);
        return getGenre(genreId).map(existing -> {
            genre.setGenreId(genreId);
            return genreRepository.save(genre);
        });
    }

    public boolean delete(int genreId) {
        if (getGenre(genreId).isPresent()) {
            genreRepository.delete(genreId);
            return true;
        }
        return false;
    }

    private void validate(Genre genre) {
        if (genre.getName() == null || genre.getName().isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (genre.getName().length() > 45) {
            throw new IllegalArgumentException("name must have at most 45 characters");
        }
        if (genre.getActive() == null) {
            genre.setActive(true);
        }
    }
}