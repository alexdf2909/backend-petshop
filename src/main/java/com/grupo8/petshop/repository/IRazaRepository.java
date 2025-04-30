package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Raza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRazaRepository extends JpaRepository<Raza, Long> {
}
