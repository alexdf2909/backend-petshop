package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Especie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEspecieRepository extends JpaRepository<Especie, Long> {
}
