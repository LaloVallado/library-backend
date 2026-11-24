package com.tecnm.merida.library_backend.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// El prestamo completo: cabecera (Maestro) + lista de libros (Detalle)
public class Loan {

    private Integer loanId;
    private Integer memberId;
    private LocalDateTime loanDate;
    private LocalDate returnDate;
    private String state;
    private String comment;
    private List<LoanItem> items;

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDateTime loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<LoanItem> getItems() {
        return items;
    }

    public void setItems(List<LoanItem> items) {
        this.items = items;
    }
}