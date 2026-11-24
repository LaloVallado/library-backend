package com.tecnm.merida.library_backend.persistence;

import com.tecnm.merida.library_backend.domain.Loan;
import com.tecnm.merida.library_backend.domain.repository.LoanRepository;
import com.tecnm.merida.library_backend.persistence.crud.PrestamoCrudRepository;
import com.tecnm.merida.library_backend.persistence.entity.Prestamo;
import com.tecnm.merida.library_backend.persistence.mapper.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PrestamoRepository implements LoanRepository {

    @Autowired
    private PrestamoCrudRepository prestamoCrudRepository;

    @Autowired
    private LoanMapper mapper;

    @Override
    public List<Loan> getAll() {
        return mapper.toLoans((List<Prestamo>) prestamoCrudRepository.findAll());
    }

    @Override
    public List<Loan> getByMember(int memberId) {
        return mapper.toLoans(prestamoCrudRepository.findByIdSocio(memberId));
    }

    @Override
    public Optional<Loan> getLoan(int loanId) {
        return prestamoCrudRepository.findById(loanId)
                .map(prestamo -> mapper.toLoan(prestamo));
    }

    @Override
    public Loan save(Loan loan) {
        // 1. Mapper: Loan (dominio) -> Prestamo (entidad) con su lista de PrestamoLibro
        Prestamo prestamo = mapper.toPrestamo(loan);

        // 2. Cada detalle debe conocer a su prestamo (Maestro) para el guardado en cascada
        prestamo.getLibros().forEach(detalle -> detalle.setPrestamo(prestamo));

        // 3. Un solo save: Hibernate inserta el prestamo y despues todos sus libros (CascadeType.ALL)
        return mapper.toLoan(prestamoCrudRepository.save(prestamo));
    }

    @Override
    public Optional<Loan> update(int loanId, Loan changes) {
        // Solo se actualiza la cabecera; los libros del prestamo no se tocan
        return prestamoCrudRepository.findById(loanId).map(prestamo -> {
            prestamo.setFechaDevolucion(changes.getReturnDate());
            prestamo.setEstado(changes.getState());
            prestamo.setComentario(changes.getComment());
            return mapper.toLoan(prestamoCrudRepository.save(prestamo));
        });
    }

    @Override
    public void delete(int loanId) {
        // Gracias a CascadeType.ALL tambien se borran sus registros en prestamos_libros
        prestamoCrudRepository.deleteById(loanId);
    }
}