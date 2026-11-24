package com.tecnm.merida.library_backend.web.controller;

import com.tecnm.merida.library_backend.domain.Book;
import com.tecnm.merida.library_backend.domain.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
@Tag(name = "Book", description = "Catalog of books")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    @Operation(summary = "Get all books", description = "Returns the list of all books with their genre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied")
    })
    public ResponseEntity<List<Book>> getAll() {
        return new ResponseEntity<>(bookService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get book by ID", description = "Returns one book by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found")
    })
    public ResponseEntity<Book> getBook(
            @Parameter(description = "ID of the book", example = "1", required = true)
            @PathVariable("id") int bookId) {
        return bookService.getBook(bookId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/genre/{genreId}")
    @Operation(summary = "Get books by genre", description = "Returns the books of a genre ordered by title")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Books of the genre retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    public ResponseEntity<List<Book>> getByGenre(
            @Parameter(description = "ID of the genre", example = "2", required = true)
            @PathVariable("genreId") int genreId) {
        return bookService.getByGenre(genreId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a book",
            description = "Registers a new book. The genreId must exist",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "New book",
                                    value = """
                                            {
                                              "title": "El Hobbit",
                                              "author": "J. R. R. Tolkien",
                                              "isbn": "9788445073728",
                                              "genreId": 1,
                                              "publicationYear": 1937,
                                              "availableCopies": 3,
                                              "active": true
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Book created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or the genre does not exist"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "409", description = "Duplicated ISBN")
    })
    public ResponseEntity<Book> save(@RequestBody Book book) {
        return new ResponseEntity<>(bookService.save(book), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a book",
            description = "Updates all the data of an existing book",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Update book",
                                    value = """
                                            {
                                              "title": "El Hobbit (edicion ilustrada)",
                                              "author": "J. R. R. Tolkien",
                                              "isbn": "9788445073728",
                                              "genreId": 2,
                                              "publicationYear": 1937,
                                              "availableCopies": 5,
                                              "active": true
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or the genre does not exist"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "Duplicated ISBN")
    })
    public ResponseEntity<Book> update(
            @Parameter(description = "ID of the book to update", example = "13", required = true)
            @PathVariable("id") int bookId,
            @RequestBody Book book) {
        return bookService.update(bookId, book)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a book", description = "Deletes a book if it exists and is not part of a loan")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Book deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Book not found"),
            @ApiResponse(responseCode = "409", description = "The book is part of a loan and cannot be deleted")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the book to delete", example = "13", required = true)
            @PathVariable("id") int bookId) {
        if (bookService.delete(bookId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}