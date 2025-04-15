package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.ColorDTO;
import com.grupo8.petshop.service.IColorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/color")
public class ColorController {
    private final IColorService colorService;

    public ColorController(IColorService colorService) {
        this.colorService = colorService;
    }

    @PostMapping
    public ResponseEntity<ColorDTO> crearColor(@RequestBody ColorDTO colorDTO) {
        ColorDTO colorARetornar = colorService.createColor(colorDTO);
        if (colorARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(colorARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<ColorDTO>> traerTodos() {
        return ResponseEntity.ok(colorService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ColorDTO> buscarColorPorId(@PathVariable Long id) {
        Optional<ColorDTO> color = colorService.searchForId(id);
        if (color.isPresent()) {
            return ResponseEntity.ok(color.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarColor(@PathVariable Long id, @RequestBody ColorDTO colorDTO) {
        try {
            colorService.updateColor(id, colorDTO);
            return ResponseEntity.ok("{\"message\": \"Color modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarColor(@PathVariable Long id) {
        Optional<ColorDTO> colorOptional = colorService.searchForId(id);
        if (colorOptional.isPresent()) {
            colorService.deleteColor(id);
            return ResponseEntity.ok("{\"message\": \"color eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"color no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
