package com.tecnm.merida.library_backend.persistence;

import com.tecnm.merida.library_backend.domain.Book;
import com.tecnm.merida.library_backend.domain.repository.BookRepository;
import com.tecnm.merida.library_backend.persistence.crud.LibroCrudRepository;
import com.tecnm.merida.library_backend.persistence.entity.Libro;
import com.tecnm.merida.library_backend.persistence.mapper.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class LibroRepository implements BookRepository {

    @Autowired
    private LibroCrudRepository libroCrudRepository;

    @Autowired
    private BookMapper mapper;

    @Override
    public List<Book> getAll() {
        List<Libro> libros = (List<Libro>) libroCrudRepository.findAll();
        return mapper.toBooks(libros);
    }

    @Override
    public Optional<Book> getBook(int bookId) {
        return libroCrudRepository.findById(bookId)
                .map(libro -> mapper.toBook(libro));
    }

    @Override
    public List<Book> getByGenre(int genreId) {
        return mapper.toBooks(libroCrudRepository.findByIdGeneroOrderByTituloAsc(genreId));
    }

    @Override
    public Book save(Book book) {
        Libro libro = mapper.toLibro(book);
        return mapper.toBook(libroCrudRepository.save(libro));
    }

    @Override
    public void delete(int bookId) {
        libroCrudRepository.deleteById(bookId);
    }
}