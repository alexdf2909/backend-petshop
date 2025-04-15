package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.ServicioDTO;
import com.grupo8.petshop.entity.Servicio;
import com.grupo8.petshop.repository.IServicioRepository;
import com.grupo8.petshop.service.IServicioService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServicioService implements IServicioService {
    private final IServicioRepository servicioRepository;

    public ServicioService(IServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    public ServicioDTO createServicio(ServicioDTO servicioDTO) {
        Servicio servicio = new Servicio();
        servicio.setNombre(servicioDTO.getNombre());
        servicio.setDescripcion(servicioDTO.getDescripcion());
        servicio.setHorario(servicioDTO.getHorario());
        servicio.setImagenAntes(servicioDTO.getImagenAntes());
        servicio.setImagenDespues(servicioDTO.getImagenDespues());

        Servicio savedServicio = servicioRepository.save(servicio);

        return convertToDTO(savedServicio);
    }

    @Override
    public Optional<ServicioDTO> searchForId(Long id) {
        return servicioRepository.findById(id).map(this::convertToDTO);
    }

    @Override
    public List<ServicioDTO> searchAll() {
        return servicioRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateServicio(Long id, ServicioDTO servicioDTO) {
        Optional<Servicio> servicioOpt = servicioRepository.findById(id);
        if (servicioOpt.isEmpty()) {
            throw new RuntimeException("Servicio no encontrada");
        }

        Servicio servicio = servicioOpt.get();

        if (servicioDTO.getNombre() != null && !servicioDTO.getNombre().isEmpty()) {
            servicio.setNombre(servicioDTO.getNombre());
        }
        if (servicioDTO.getDescripcion() != null && !servicioDTO.getDescripcion().isEmpty()) {
            servicio.setDescripcion(servicioDTO.getDescripcion());
        }
        if (servicioDTO.getHorario() != null && !servicioDTO.getHorario().isEmpty()) {
            servicio.setHorario(servicioDTO.getHorario());
        }
        if (servicioDTO.getImagenAntes() != null && !servicioDTO.getImagenAntes().isEmpty()) {
            servicio.setImagenAntes(servicioDTO.getImagenAntes());
        }
        if (servicioDTO.getImagenDespues() != null && !servicioDTO.getImagenDespues().isEmpty()) {
            servicio.setImagenDespues(servicioDTO.getImagenDespues());
        }

        servicioRepository.save(servicio);
    }

    @Override
    public void deleteServicio(Long id) {
        // Buscar la categoría a eliminar
        Optional<Servicio> servicioOpt = servicioRepository.findById(id);
        if (servicioOpt.isEmpty()) {
            throw new RuntimeException("Servicio no encontrado");
        }
        Servicio servicio = servicioOpt.get();

        servicioRepository.delete(servicio);
    }

    private ServicioDTO convertToDTO(Servicio servicio) {
        return new ServicioDTO(
                servicio.getServicioId(),
                servicio.getNombre(),
                servicio.getDescripcion(),
                servicio.getImagenAntes(),
                servicio.getImagenDespues(),
                servicio.getHorario()
        );
    }
}