package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Color;
import com.grupo8.petshop.entity.Compra;
import com.grupo8.petshop.entity.Usuario;
import com.grupo8.petshop.entity.Variante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICompraRepository extends JpaRepository<Compra, Long> {

}
