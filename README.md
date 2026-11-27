# Library Backend API

Final project for **Backend Development** – Instituto Tecnologico de Merida, group 8SC.
Author: **Salvador Eduardo Vallado Villamonte**.

REST API to manage a library: genres, books, members and book loans.
A loan is a master-detail record: one header (member, dates, state) with a list of books,
saved in a single transaction using cascade persistence.

## Tech stack

- Java 25, Spring Boot 4.1.1, Gradle
- PostgreSQL, Spring Data JPA / Hibernate
- MapStruct (entity <-> domain mapping)
- springdoc-openapi (Swagger UI)
- Spring Security + JWT (jjwt 0.12.6), BCrypt passwords

## Architecture

- `domain`: business models, DTOs, repository interfaces and services (validations, @Transactional)
- `persistence`: JPA entities, Spring Data CRUD repositories, MapStruct mappers and repository implementations
- `web`: REST controllers documented with Swagger, global exception handler and JWT security

## Setup

1. Create a PostgreSQL database named `library_backend`.
2. Run the script `database/library_backend.sql` (tables and sample data).
3. Create `src/main/resources/application-dev.properties` with `spring.datasource.url`, `spring.datasource.username` and `spring.datasource.password` (this file is ignored by Git).
4. Run `LibraryBackendApplication` and open `http://localhost:8080/swagger-ui.html`.

## Authentication

1. `POST /auth/login` with `eduardo@biblioteca.com` / `1234`.
2. Copy the token, click **Authorize** and paste it.
3. All the other endpoints require the token; without it they return 401.

## Endpoints

| Resource | Endpoints |
| --- | --- |
| Auth | POST /auth/login |
| Genres | GET, POST /genres - GET, PUT, DELETE /genres/{id} |
| Books | GET, POST /books - GET, PUT, DELETE /books/{id} - GET /books/genre/{genreId} |
| Members | GET, POST /members - GET, PUT, DELETE /members/{id} |
| Loans | GET, POST /loans - GET, PUT, DELETE /loans/{id} - GET /loans/member/{memberId} |