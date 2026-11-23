package com.tecnm.merida.library_backend.persistence.crud;

import com.tecnm.merida.library_backend.persistence.entity.Socio;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SocioCrudRepository extends CrudRepository<Socio, Integer> {

    // Para el login: buscar al socio por su correo
    Optional<Socio> findByCorreoElectronico(String correoElectronico);
}