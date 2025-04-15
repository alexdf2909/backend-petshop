package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEtiquetaRepository extends JpaRepository<Etiqueta, Long> {
}
