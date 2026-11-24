package com.tecnm.merida.library_backend.domain.repository;

import com.tecnm.merida.library_backend.domain.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanRepository {

    List<Loan> getAll();
    List<Loan> getByMember(int memberId);
    Optional<Loan> getLoan(int loanId);
    Loan save(Loan loan);
    Optional<Loan> update(int loanId, Loan changes);
    void delete(int loanId);
}