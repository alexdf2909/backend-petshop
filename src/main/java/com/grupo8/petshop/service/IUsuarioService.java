package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ColorDTO;
import com.grupo8.petshop.dto.UsuarioDTO;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    UsuarioDTO createUsuario(UsuarioDTO usuarioDTO);
    Optional<UsuarioDTO> searchForId(Long id);
    List<UsuarioDTO> searchAll();
    void updateUsuario(Long id, UsuarioDTO usuarioDTO);
    void deleteUsuario(Long id);
    Optional<UsuarioDTO> addFavorite(Long usuarioId, Long productoId);
    Optional<UsuarioDTO> removeFavorite(Long usuarioId, Long productoId);
}
