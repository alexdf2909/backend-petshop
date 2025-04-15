package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IMarcaRepository extends JpaRepository<Marca, Long> {
}
