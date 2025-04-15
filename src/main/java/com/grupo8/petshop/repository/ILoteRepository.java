package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILoteRepository extends JpaRepository<Lote, Long> {
    List<Lote> findByCompra(Compra compra);
    List<Lote> findByVariante(Variante variante);
}
