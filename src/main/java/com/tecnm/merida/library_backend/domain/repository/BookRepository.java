package com.tecnm.merida.library_backend.domain.repository;

import com.tecnm.merida.library_backend.domain.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> getAll();
    Optional<Book> getBook(int bookId);
    List<Book> getByGenre(int genreId);
    Book save(Book book);
    void delete(int bookId);
}