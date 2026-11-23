package com.tecnm.merida.library_backend.persistence.crud;

import com.tecnm.merida.library_backend.persistence.entity.Genero;
import org.springframework.data.repository.CrudRepository;

public interface GeneroCrudRepository extends CrudRepository<Genero, Integer> {
}