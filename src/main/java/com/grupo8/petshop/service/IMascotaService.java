package com.grupo8.petshop.service;

import com.grupo8.petshop.dto.MascotaDTO;
import com.grupo8.petshop.entity.Mascota;

import java.util.List;
import java.util.Optional;

public interface IMascotaService {
    Mascota createMascota(MascotaDTO mascotaDTO);
    Optional<Mascota> searchForId(Long id);
    List<Mascota> searchAll();
    void updateMascota(Long id, MascotaDTO mascotaDTO);
    void deleteMascota(Long id);
    List<Mascota> obtenerMascotasDelUsuarioAutenticado();
}
