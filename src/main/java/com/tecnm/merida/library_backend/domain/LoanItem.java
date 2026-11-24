package com.tecnm.merida.library_backend.domain;

// Un renglon del detalle del prestamo: que libro y cuantas copias
public class LoanItem {

    private Integer bookId;
    private String bookTitle;
    private Integer quantity;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}