package com.tecnm.merida.library_backend.domain.service;

import com.tecnm.merida.library_backend.domain.Book;
import com.tecnm.merida.library_backend.domain.Loan;
import com.tecnm.merida.library_backend.domain.LoanItem;
import com.tecnm.merida.library_backend.domain.dto.LoanItemRequestDTO;
import com.tecnm.merida.library_backend.domain.dto.LoanRequestDTO;
import com.tecnm.merida.library_backend.domain.dto.LoanUpdateDTO;
import com.tecnm.merida.library_backend.domain.repository.BookRepository;
import com.tecnm.merida.library_backend.domain.repository.LoanRepository;
import com.tecnm.merida.library_backend.domain.repository.MemberRepository;
import com.tecnm.merida.library_backend.persistence.mapper.LoanRequestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class LoanService {

    public static final String ACTIVE = "ACTIVO";
    public static final String RETURNED = "DEVUELTO";

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRequestMapper loanRequestMapper;

    public List<Loan> getAll() {
        return loanRepository.getAll();
    }

    public Optional<Loan> getLoan(int loanId) {
        return loanRepository.getLoan(loanId);
    }

    // Si el socio no existe regresa vacio (el controlador responde 404)
    public Optional<List<Loan>> getByMember(int memberId) {
        if (memberRepository.getMember(memberId).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(loanRepository.getByMember(memberId));
    }

    // MAESTRO-DETALLE en una sola transaccion: todo o nada.
    // Si algo falla, se deshace todo (rollback): ni prestamo, ni detalle, ni copias descontadas.
    @Transactional
    public Loan save(LoanRequestDTO request) {
        // 1. Validar y obtener los libros pedidos
        Map<Integer, Book> books = validate(request);

        // 2. Mapper: DTO complejo -> Loan (cabecera + lista de items)
        Loan loan = loanRequestMapper.toLoan(request);

        // 3. Datos que pone el sistema
        loan.setLoanDate(LocalDateTime.now());
        loan.setState(ACTIVE);
        if (loan.getReturnDate() == null) {
            loan.setReturnDate(LocalDate.now().plusDays(14)); // 2 semanas por defecto
        }

        // 4. Descontar las copias prestadas de cada libro
        for (LoanItem item : loan.getItems()) {
            Book book = books.get(item.getBookId());
            book.setAvailableCopies(book.getAvailableCopies() - item.getQuantity());
            bookRepository.save(book);
        }

        // 5. Un solo save: la cascada guarda el prestamo y sus libros
        Loan saved = loanRepository.save(loan);

        // 6. Agregar el titulo de cada libro a la respuesta
        saved.getItems().forEach(item -> item.setBookTitle(books.get(item.getBookId()).getTitle()));
        return saved;
    }

    // Solo cambia la cabecera. Si pasa de ACTIVO a DEVUELTO, regresa las copias.
    @Transactional
    public Optional<Loan> update(int loanId, LoanUpdateDTO request) {
        Optional<Loan> current = loanRepository.getLoan(loanId);
        if (current.isEmpty()) {
            return Optional.empty();
        }
        Loan loan = current.get();

        String newState = request.getEstado() == null ? loan.getState() : request.getEstado().trim().toUpperCase();
        if (!ACTIVE.equals(newState) && !RETURNED.equals(newState)) {
            throw new IllegalArgumentException("estado must be ACTIVO or DEVUELTO");
        }
        if (RETURNED.equals(loan.getState()) && ACTIVE.equals(newState)) {
            throw new IllegalArgumentException("A returned loan cannot be reopened");
        }

        LocalDate newReturnDate = request.getFechaDevolucion() == null ? loan.getReturnDate() : request.getFechaDevolucion();
        if (newReturnDate.isBefore(loan.getLoanDate().toLocalDate())) {
            throw new IllegalArgumentException("fechaDevolucion cannot be before the loan date");
        }

        String newComment = request.getComentario() == null ? loan.getComment() : request.getComentario();
        if (newComment != null && newComment.length() > 300) {
            throw new IllegalArgumentException("comentario must have at most 300 characters");
        }

        if (ACTIVE.equals(loan.getState()) && RETURNED.equals(newState)) {
            restoreCopies(loan);
        }

        Loan changes = new Loan();
        changes.setReturnDate(newReturnDate);
        changes.setState(newState);
        changes.setComment(newComment);
        return loanRepository.update(loanId, changes);
    }

    // Borra el prestamo y, por cascada, su detalle. Si estaba ACTIVO, regresa las copias.
    @Transactional
    public boolean delete(int loanId) {
        Optional<Loan> loan = loanRepository.getLoan(loanId);
        if (loan.isEmpty()) {
            return false;
        }
        if (ACTIVE.equals(loan.get().getState())) {
            restoreCopies(loan.get());
        }
        loanRepository.delete(loanId);
        return true;
    }

    private void restoreCopies(Loan loan) {
        for (LoanItem item : loan.getItems()) {
            bookRepository.getBook(item.getBookId()).ifPresent(book -> {
                book.setAvailableCopies(book.getAvailableCopies() + item.getQuantity());
                bookRepository.save(book);
            });
        }
    }

    // Validaciones: cualquier dato invalido regresa 400 (nunca 500)
    private Map<Integer, Book> validate(LoanRequestDTO request) {
        if (request.getIdSocio() == null || memberRepository.getMember(request.getIdSocio()).isEmpty()) {
            throw new IllegalArgumentException("Member " + request.getIdSocio() + " does not exist");
        }
        if (request.getFechaDevolucion() != null && request.getFechaDevolucion().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("fechaDevolucion cannot be in the past");
        }
        if (request.getComentario() != null && request.getComentario().length() > 300) {
            throw new IllegalArgumentException("comentario must have at most 300 characters");
        }
        if (request.getLibrosPrestados() == null || request.getLibrosPrestados().isEmpty()) {
            throw new IllegalArgumentException("The loan must have at least one book");
        }

        Map<Integer, Book> books = new HashMap<>();
        for (LoanItemRequestDTO item : request.getLibrosPrestados()) {
            if (item.getIdLibro() == null) {
                throw new IllegalArgumentException("idLibro is required");
            }
            if (books.containsKey(item.getIdLibro())) {
                throw new IllegalArgumentException("Book " + item.getIdLibro() + " is repeated in the loan");
            }
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new IllegalArgumentException("cantidad must be greater than 0");
            }

            Book book = bookRepository.getBook(item.getIdLibro())
                    .orElseThrow(() -> new IllegalArgumentException("Book " + item.getIdLibro() + " does not exist"));

            if (!Boolean.TRUE.equals(book.getActive())) {
                throw new IllegalArgumentException("Book '" + book.getTitle() + "' is not active");
            }
            if (book.getAvailableCopies() < item.getCantidad()) {
                throw new IllegalArgumentException("Not enough copies of '" + book.getTitle()
                        + "': available " + book.getAvailableCopies());
            }
            books.put(item.getIdLibro(), book);
        }
        return books;
    }
}