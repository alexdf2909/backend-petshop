package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.entidades.HistorialInteraccionDTO;
import com.grupo8.petshop.entity.HistorialInteraccion;

import java.util.List;
import java.util.Optional;

public interface IHistorialInteraccionService {
    HistorialInteraccion createHistorialInteraccion(HistorialInteraccionDTO historialInteraccionDTO);
    Optional<HistorialInteraccion> searchForId(Long id);
    List<HistorialInteraccion> searchAll();
    List<HistorialInteraccion> searchByUsuario(Long usuarioId);
    List<HistorialInteraccion> obtenerHistorialInteraccionsDelUsuarioAutenticado();
}
