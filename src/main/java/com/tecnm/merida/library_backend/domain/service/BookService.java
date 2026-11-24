package com.tecnm.merida.library_backend.domain.service;

import com.tecnm.merida.library_backend.domain.Book;
import com.tecnm.merida.library_backend.domain.repository.BookRepository;
import com.tecnm.merida.library_backend.domain.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    public List<Book> getAll() {
        return bookRepository.getAll();
    }

    public Optional<Book> getBook(int bookId) {
        return bookRepository.getBook(bookId);
    }

    // Si el genero no existe regresa vacio (el controlador responde 404)
    public Optional<List<Book>> getByGenre(int genreId) {
        if (genreRepository.getGenre(genreId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(bookRepository.getByGenre(genreId));
    }

    public Book save(Book book) {
        validate(book);
        book.setBookId(null); // en POST el id lo genera la base de datos
        return bookRepository.save(book);
    }

    public Optional<Book> update(int bookId, Book book) {
        validate(book);
        return getBook(bookId).map(existing -> {
            book.setBookId(bookId);
            return bookRepository.save(book);
        });
    }

    public boolean delete(int bookId) {
        if (getBook(bookId).isPresent()) {
            bookRepository.delete(bookId);
            return true;
        }
        return false;
    }

    // Validaciones para que los datos invalidos regresen 400 y nunca 500
    private void validate(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        if (book.getTitle().length() > 150) {
            throw new IllegalArgumentException("title must have at most 150 characters");
        }
        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new IllegalArgumentException("author is required");
        }
        if (book.getAuthor().length() > 100) {
            throw new IllegalArgumentException("author must have at most 100 characters");
        }
        if (book.getIsbn() != null && book.getIsbn().length() > 20) {
            throw new IllegalArgumentException("isbn must have at most 20 characters");
        }
        if (book.getGenreId() == null || genreRepository.getGenre(book.getGenreId()).isEmpty()) {
            throw new IllegalArgumentException("Genre " + book.getGenreId() + " does not exist");
        }
        if (book.getPublicationYear() != null
                && (book.getPublicationYear() < 0 || book.getPublicationYear() > LocalDate.now().getYear())) {
            throw new IllegalArgumentException("publicationYear is not valid");
        }
        if (book.getAvailableCopies() == null) {
            book.setAvailableCopies(0);
        }
        if (book.getAvailableCopies() < 0) {
            throw new IllegalArgumentException("availableCopies cannot be negative");
        }
        if (book.getActive() == null) {
            book.setActive(true);
        }
    }
}