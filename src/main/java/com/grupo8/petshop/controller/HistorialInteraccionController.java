package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.HistorialInteraccionDTO;
import com.grupo8.petshop.entity.HistorialInteraccion;
import com.grupo8.petshop.service.IHistorialInteraccionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historial")
public class HistorialInteraccionController {
    private final IHistorialInteraccionService historialInteraccionService;

    public HistorialInteraccionController(IHistorialInteraccionService historialInteraccionService) {
        this.historialInteraccionService = historialInteraccionService;
    }

    @PostMapping
    public ResponseEntity<HistorialInteraccion> crearHistorialInteraccion(@RequestBody HistorialInteraccionDTO historialInteraccionDTO) {
        HistorialInteraccion historialInteraccionARetornar = historialInteraccionService.createHistorialInteraccion(historialInteraccionDTO);
        if (historialInteraccionARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(historialInteraccionARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<HistorialInteraccion>> traerTodos() {
        return ResponseEntity.ok(historialInteraccionService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HistorialInteraccion> buscarHistorialInteraccionPorId(@PathVariable Long id) {
        Optional<HistorialInteraccion> historialInteraccion = historialInteraccionService.searchForId(id);
        if (historialInteraccion.isPresent()) {
            return ResponseEntity.ok(historialInteraccion.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("/mi-historial")
    public ResponseEntity<List<HistorialInteraccion>> obtenerHistorialInteraccionsDelUsuario() {
        try {
            List<HistorialInteraccion> historialInteraccions = historialInteraccionService.obtenerHistorialInteraccionsDelUsuarioAutenticado();
            return ResponseEntity.ok(historialInteraccions);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<HistorialInteraccion>> getHistorialInteraccionsByUsuario(@PathVariable Long usuarioId) {
        List<HistorialInteraccion> historialInteraccions = historialInteraccionService.searchByUsuario(usuarioId);
        return ResponseEntity.ok(historialInteraccions);
    }
}