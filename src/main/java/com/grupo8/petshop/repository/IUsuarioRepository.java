package com.grupo8.petshop.repository;

import com.grupo8.petshop.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.mascotas WHERE u.usuarioId = :id")
    Optional<Usuario> findByIdWithMascotas(Long id);

    List<Usuario> findByMascotas_Raza_Nombre(String nombreRaza);
}
