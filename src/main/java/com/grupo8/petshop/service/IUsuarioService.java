package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.entidades.UsuarioDTO;
import com.grupo8.petshop.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    Usuario createUsuario(UsuarioDTO usuarioDTO);
    Optional<Usuario> searchForId(Long id);
    List<Usuario> searchAll();
    void updateUsuario(Long id, UsuarioDTO usuarioDTO);
    void deleteUsuario(Long id);
    Optional<Usuario> addFavorite(Long usuarioId, Long productoId);
    Optional<Usuario> removeFavorite(Long usuarioId, Long productoId);

    // Obtener el perfil del cliente autenticado
    Optional<Usuario> getPerfilCliente();

    // Actualizar el perfil del cliente autenticado
    Usuario actualizarPerfilCliente(Usuario usuarioActualizado);
}
