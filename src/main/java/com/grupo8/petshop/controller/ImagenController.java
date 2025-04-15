package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.ImagenDTO;
import com.grupo8.petshop.entity.Imagen;
import com.grupo8.petshop.service.IImagenService;
import com.grupo8.petshop.service.imp.CloudinaryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/imagen")
public class ImagenController {
    private final IImagenService imagenService;
    private final CloudinaryService cloudinaryService;

    public ImagenController(IImagenService imagenService, CloudinaryService cloudinaryService) {
        this.imagenService = imagenService;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImagen(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.subirImagen(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(url);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Imagen> crearImagen(@RequestBody ImagenDTO imagenDTO) {
        Imagen imagenARetornar = imagenService.createImagen(imagenDTO);
        if (imagenARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(imagenARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Imagen>> traerTodos() {
        return ResponseEntity.ok(imagenService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Imagen> buscarImagenPorId(@PathVariable Long id) {
        Optional<Imagen> imagen = imagenService.searchForId(id);
        if (imagen.isPresent()) {
            return ResponseEntity.ok(imagen.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarImagen(@PathVariable Long id, @RequestBody ImagenDTO imagenDTO) {
        try {
            imagenService.updateImagen(id, imagenDTO);
            return ResponseEntity.ok("{\"message\": \"Imagen modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarImagen(@PathVariable Long id) {
        Optional<Imagen> imagenOptional = imagenService.searchForId(id);
        if (imagenOptional.isPresent()) {
            imagenService.deleteImagen(id);
            return ResponseEntity.ok("{\"message\": \"imagen eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"imagen no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}