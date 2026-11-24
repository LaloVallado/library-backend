package com.tecnm.merida.library_backend.web.controller;

import com.tecnm.merida.library_backend.domain.Loan;
import com.tecnm.merida.library_backend.domain.dto.LoanRequestDTO;
import com.tecnm.merida.library_backend.domain.dto.LoanUpdateDTO;
import com.tecnm.merida.library_backend.domain.service.LoanService;
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
@RequestMapping("/loans")
@Tag(name = "Loan", description = "Book loans (master-detail): header + list of books in one transaction")
@SecurityRequirement(name = "bearerAuth")
public class LoanController {

    private final LoanService loanService;

    @Autowired
    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping("")
    @Operation(summary = "Get all loans", description = "Returns all loans with their books")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied")
    })
    public ResponseEntity<List<Loan>> getAll() {
        return new ResponseEntity<>(loanService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get loan by ID", description = "Returns one loan with its books")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<Loan> getLoan(
            @Parameter(description = "ID of the loan", example = "1", required = true)
            @PathVariable("id") int loanId) {
        return loanService.getLoan(loanId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/member/{memberId}")
    @Operation(summary = "Get loans by member", description = "Returns all the loans of one member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loans of the member retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<List<Loan>> getByMember(
            @Parameter(description = "ID of the member", example = "1", required = true)
            @PathVariable("memberId") int memberId) {
        return loanService.getByMember(memberId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a loan (master-detail)",
            description = "Receives the loan header and its list of books in ONE nested JSON. "
                    + "Everything is saved in one @Transactional operation using CascadeType.ALL: "
                    + "the loan, its books and the discount of available copies. If anything fails, nothing is saved.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "New loan with 2 books",
                                    value = """
                                            {
                                              "idSocio": 1,
                                              "fechaDevolucion": "2026-12-15",
                                              "comentario": "Prestamo para tarea de literatura",
                                              "librosPrestados": [
                                                {
                                                  "idLibro": 2,
                                                  "cantidad": 1
                                                },
                                                {
                                                  "idLibro": 7,
                                                  "cantidad": 1
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Loan and its books created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data: member or book does not exist, not enough copies, empty list, etc."),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied")
    })
    public ResponseEntity<Loan> save(@RequestBody LoanRequestDTO request) {
        return new ResponseEntity<>(loanService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a loan header",
            description = "Updates the return date, state and comment. "
                    + "estado: ACTIVO or DEVUELTO. Changing to DEVUELTO gives the copies back to the books.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Return the loan",
                                    value = """
                                            {
                                              "fechaDevolucion": "2026-12-20",
                                              "estado": "DEVUELTO",
                                              "comentario": "Devuelto en buen estado"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data or invalid state change"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<Loan> update(
            @Parameter(description = "ID of the loan to update", example = "2", required = true)
            @PathVariable("id") int loanId,
            @RequestBody LoanUpdateDTO request) {
        return loanService.update(loanId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a loan", description = "Deletes a loan and, by cascade, all its books. If it was ACTIVO, the copies are returned")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Loan deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the loan to delete", example = "2", required = true)
            @PathVariable("id") int loanId) {
        if (loanService.delete(loanId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}