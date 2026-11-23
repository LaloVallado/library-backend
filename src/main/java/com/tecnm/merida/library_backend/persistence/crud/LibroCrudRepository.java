package com.tecnm.merida.library_backend.persistence.crud;

import com.tecnm.merida.library_backend.persistence.entity.Libro;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LibroCrudRepository extends CrudRepository<Libro, Integer> {

    // Query method: libros de un genero ordenados por titulo
    List<Libro> findByIdGeneroOrderByTituloAsc(Integer idGenero);
}