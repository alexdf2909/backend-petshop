package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.ImagenDTO;
import com.grupo8.petshop.entity.Imagen;
import com.grupo8.petshop.repository.IImagenRepository;
import com.grupo8.petshop.service.IImagenService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ImagenService implements IImagenService {
    private final IImagenRepository imagenRepository;

    public ImagenService(IImagenRepository imagenRepository) {
        this.imagenRepository = imagenRepository;
    }

    @Override
    public Imagen createImagen(ImagenDTO imagenDTO) {
        Imagen imagen = new Imagen();
        imagen.setImagenUrl(imagenDTO.getImagenUrl());

        return imagenRepository.save(imagen);
    }

    @Override
    public Optional<Imagen> searchForId(Long id) {
        return imagenRepository.findById(id);
    }

    @Override
    public List<Imagen> searchAll() {
        return imagenRepository.findAll();
    }

    @Override
    public void updateImagen(Long id, ImagenDTO imagenDTO) {
        Optional<Imagen> imagenOpt = imagenRepository.findById(id);
        if (imagenOpt.isEmpty()) {
            throw new RuntimeException("Imagen no encontrada");
        }

        Imagen imagen = imagenOpt.get();

        if (imagenDTO.getImagenUrl() != null && !imagenDTO.getImagenUrl().isEmpty()) {
            imagen.setImagenUrl(imagenDTO.getImagenUrl());
        }

        imagenRepository.save(imagen);
    }

    @Override
    public void deleteImagen(Long id) {
        // Buscar la categoría a eliminar
        Optional<Imagen> imagenOpt = imagenRepository.findById(id);
        if (imagenOpt.isEmpty()) {
            throw new RuntimeException("Imagen no encontrada");
        }
        Imagen imagen = imagenOpt.get();

        imagenRepository.delete(imagen);
    }

    private ImagenDTO convertToDTO(Imagen imagen) {
        return new ImagenDTO(
                imagen.getImagenId(),
                imagen.getImagenUrl()
        );
    }
}
