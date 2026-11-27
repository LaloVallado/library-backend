-- =========================================
-- TABLAS
-- =========================================
CREATE TABLE generos (
                         id_genero   SERIAL PRIMARY KEY,
                         nombre      VARCHAR(45)  NOT NULL,
                         estado      BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE libros (
                        id_libro            SERIAL PRIMARY KEY,
                        titulo              VARCHAR(150) NOT NULL,
                        autor               VARCHAR(100) NOT NULL,
                        isbn                VARCHAR(20)  UNIQUE,
                        id_genero           INTEGER      NOT NULL REFERENCES generos (id_genero),
                        anio_publicacion    INTEGER,
                        copias_disponibles  INTEGER      NOT NULL DEFAULT 0,
                        estado              BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE TABLE socios (
                        id_socio            SERIAL PRIMARY KEY,
                        nombre              VARCHAR(60)  NOT NULL,
                        apellidos           VARCHAR(100),
                        telefono            VARCHAR(15),
                        direccion           VARCHAR(100),
                        correo_electronico  VARCHAR(100) NOT NULL UNIQUE,
                        password            VARCHAR(100)
);

CREATE TABLE prestamos (
                           id_prestamo       SERIAL PRIMARY KEY,
                           id_socio          INTEGER      NOT NULL REFERENCES socios (id_socio),
                           fecha_prestamo    TIMESTAMP    NOT NULL,
                           fecha_devolucion  DATE         NOT NULL,
                           estado            VARCHAR(10)  NOT NULL,
                           comentario        VARCHAR(300)
);

CREATE TABLE prestamos_libros (
                                  id_detalle   SERIAL PRIMARY KEY,
                                  id_prestamo  INTEGER NOT NULL REFERENCES prestamos (id_prestamo),
                                  id_libro     INTEGER NOT NULL REFERENCES libros (id_libro),
                                  cantidad     INTEGER NOT NULL,
                                  UNIQUE (id_prestamo, id_libro)
);

-- =========================================
-- DATOS DE PRUEBA
-- =========================================
INSERT INTO generos (nombre, estado) VALUES
                                         ('Novela', TRUE),
                                         ('Ciencia ficcion', TRUE),
                                         ('Historia', TRUE),
                                         ('Programacion', TRUE),
                                         ('Infantil', TRUE);

INSERT INTO libros (titulo, autor, isbn, id_genero, anio_publicacion, copias_disponibles, estado) VALUES
                                                                                                      ('Cien anos de soledad', 'Gabriel Garcia Marquez', '9780307474728', 1, 1967, 5, TRUE),
                                                                                                      ('Pedro Paramo', 'Juan Rulfo', '9786071631374', 1, 1955, 3, TRUE),
                                                                                                      ('Don Quijote de la Mancha', 'Miguel de Cervantes', '9788420412146', 1, 1605, 2, TRUE),
                                                                                                      ('Dune', 'Frank Herbert', '9780441172719', 2, 1965, 4, TRUE),
                                                                                                      ('Fundacion', 'Isaac Asimov', '9788497599245', 2, 1951, 3, TRUE),
                                                                                                      ('1984', 'George Orwell', '9780451524935', 2, 1949, 6, TRUE),
                                                                                                      ('Sapiens', 'Yuval Noah Harari', '9780062316097', 3, 2011, 4, TRUE),
                                                                                                      ('La vision de los vencidos', 'Miguel Leon-Portilla', '9786070201035', 3, 1959, 2, TRUE),
                                                                                                      ('Clean Code', 'Robert C. Martin', '9780132350884', 4, 2008, 3, TRUE),
                                                                                                      ('Spring Boot in Action', 'Craig Walls', '9781617292545', 4, 2016, 2, TRUE),
                                                                                                      ('El principito', 'Antoine de Saint-Exupery', '9788478887194', 5, 1943, 5, TRUE),
                                                                                                      ('Harry Potter y la piedra filosofal', 'J. K. Rowling', '9788478884452', 5, 1997, 4, TRUE);

-- Contrasena de los socios: 1234 (encriptada con BCrypt)
INSERT INTO socios (nombre, apellidos, telefono, direccion, correo_electronico, password) VALUES
                                                                                              ('Eduardo', 'Vallado Villamonte', '9991234567', 'Calle 60 # 123, Merida', 'eduardo@biblioteca.com', '$2a$10$p8qcCOz8Z0fEL2YSKVNOMeN7dpTn7BxF27F5QQeELSGW40U.G5o4C'),
                                                                                              ('Ana', 'Lopez Perez', '9997654321', 'Calle 21 # 45, Merida', 'ana@biblioteca.com', '$2a$10$p8qcCOz8Z0fEL2YSKVNOMeN7dpTn7BxF27F5QQeELSGW40U.G5o4C'),
                                                                                              ('Carlos', 'Ruiz Canul', '9995551234', 'Calle 50 # 300, Merida', 'carlos@biblioteca.com', NULL);

-- Un prestamo de ejemplo (Maestro) con 2 libros (Detalle)
INSERT INTO prestamos (id_socio, fecha_prestamo, fecha_devolucion, estado, comentario) VALUES
    (1, '2026-11-20 10:00:00', '2026-12-04', 'ACTIVO', 'Prestamo de ejemplo');

INSERT INTO prestamos_libros (id_prestamo, id_libro, cantidad) VALUES
                                                                   (1, 1, 1),
                                                                   (1, 4, 1);

-- Se descuentan las copias prestadas en el ejemplo
UPDATE libros SET copias_disponibles = copias_disponibles - 1 WHERE id_libro IN (1, 4);