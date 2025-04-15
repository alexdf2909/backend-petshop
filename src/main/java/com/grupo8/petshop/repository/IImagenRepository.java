package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IImagenRepository extends JpaRepository<Imagen, Long> {
}
