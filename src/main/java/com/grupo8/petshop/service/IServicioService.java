package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ServicioDTO;

import java.util.List;
import java.util.Optional;

public interface IServicioService {
    ServicioDTO createServicio(ServicioDTO servicioDTO);
    Optional<ServicioDTO> searchForId(Long id);
    List<ServicioDTO> searchAll();
    void updateServicio(Long id, ServicioDTO servicioDTO);
    void deleteServicio(Long id);
}
