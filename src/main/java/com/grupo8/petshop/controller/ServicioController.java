package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.ServicioDTO;
import com.grupo8.petshop.service.IServicioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/servicio")
public class ServicioController {
    private final IServicioService servicioService;

    public ServicioController(IServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @PostMapping
    public ResponseEntity<ServicioDTO> crearServicio(@RequestBody ServicioDTO servicioDTO) {
        ServicioDTO servicioARetornar = servicioService.createServicio(servicioDTO);
        if (servicioARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(servicioARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<ServicioDTO>> traerTodos() {
        return ResponseEntity.ok(servicioService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioDTO> buscarServicioPorId(@PathVariable Long id) {
        Optional<ServicioDTO> servicio = servicioService.searchForId(id);
        if (servicio.isPresent()) {
            return ResponseEntity.ok(servicio.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarServicio(@PathVariable Long id, @RequestBody ServicioDTO servicioDTO) {
        try {
            servicioService.updateServicio(id, servicioDTO);
            return ResponseEntity.ok("{\"message\": \"Servicio modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarServicio(@PathVariable Long id) {
        Optional<ServicioDTO> servicioOptional = servicioService.searchForId(id);
        if (servicioOptional.isPresent()) {
            servicioService.deleteServicio(id);
            return ResponseEntity.ok("{\"message\": \"servicio eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"servicio no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
