package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IMascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByUsuario(Usuario usuario);
}
