package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Peso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPesoRepository extends JpaRepository<Peso, Long> {
}
