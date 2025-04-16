package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ColorDTO;
import com.grupo8.petshop.dto.UsuarioDTO;
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
}
