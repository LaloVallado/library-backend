package com.tecnm.merida.library_backend.persistence.crud;

import com.tecnm.merida.library_backend.persistence.entity.Prestamo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PrestamoCrudRepository extends CrudRepository<Prestamo, Integer> {

    // Query method: prestamos de un socio
    List<Prestamo> findByIdSocio(Integer idSocio);
}