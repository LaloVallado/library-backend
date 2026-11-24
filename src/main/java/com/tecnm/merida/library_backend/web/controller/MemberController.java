package com.tecnm.merida.library_backend.web.controller;

import com.tecnm.merida.library_backend.domain.Member;
import com.tecnm.merida.library_backend.domain.service.MemberService;
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
@RequestMapping("/members")
@Tag(name = "Member", description = "Catalog of library members")
@SecurityRequirement(name = "bearerAuth")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("")
    @Operation(summary = "Get all members", description = "Returns the list of all members (passwords are never returned)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied")
    })
    public ResponseEntity<List<Member>> getAll() {
        return new ResponseEntity<>(memberService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get member by ID", description = "Returns one member by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Member not found")
    })
    public ResponseEntity<Member> getMember(
            @Parameter(description = "ID of the member", example = "1", required = true)
            @PathVariable("id") int memberId) {
        return memberService.getMember(memberId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("")
    @Operation(
            summary = "Create a member",
            description = "Registers a new member. The email must be unique",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "New member",
                                    value = """
                                            {
                                              "name": "Luis",
                                              "lastName": "Pech Chan",
                                              "phone": "9998887766",
                                              "address": "Calle 10 # 20, Merida",
                                              "email": "luis@biblioteca.com"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Member created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "409", description = "The email is already registered")
    })
    public ResponseEntity<Member> save(@RequestBody Member member) {
        return new ResponseEntity<>(memberService.save(member), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a member",
            description = "Updates the data of an existing member (the password is kept)",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Update member",
                                    value = """
                                            {
                                              "name": "Luis Alberto",
                                              "lastName": "Pech Chan",
                                              "phone": "9991112233",
                                              "address": "Calle 15 # 80, Merida",
                                              "email": "luis.pech@biblioteca.com"
                                            }
                                            """
                            )
                    )
            )
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "409", description = "The email is already registered")
    })
    public ResponseEntity<Member> update(
            @Parameter(description = "ID of the member to update", example = "4", required = true)
            @PathVariable("id") int memberId,
            @RequestBody Member member) {
        return memberService.update(memberId, member)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a member", description = "Deletes a member if it exists and has no loans")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Member deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format"),
            @ApiResponse(responseCode = "401", description = "Unauthorized: missing or invalid token"),
            @ApiResponse(responseCode = "403", description = "Forbidden: access denied"),
            @ApiResponse(responseCode = "404", description = "Member not found"),
            @ApiResponse(responseCode = "409", description = "The member has loans and cannot be deleted")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID of the member to delete", example = "4", required = true)
            @PathVariable("id") int memberId) {
        if (memberService.delete(memberId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}