package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {
}
