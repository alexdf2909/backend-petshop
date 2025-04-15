package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.ServicioDTO;
import com.grupo8.petshop.entity.Servicio;

import java.util.List;
import java.util.Optional;

public interface IServicioService {
    Servicio createServicio(ServicioDTO servicioDTO);
    Optional<Servicio> searchForId(Long id);
    List<Servicio> searchAll();
    void updateServicio(Long id, ServicioDTO servicioDTO);
    void deleteServicio(Long id);
}
