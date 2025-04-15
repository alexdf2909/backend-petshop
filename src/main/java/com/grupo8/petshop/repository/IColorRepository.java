package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IColorRepository extends JpaRepository<Color, Long> {
}
