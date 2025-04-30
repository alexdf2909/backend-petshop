package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.MascotaDTO;
import com.grupo8.petshop.entity.Mascota;
import com.grupo8.petshop.service.IMascotaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/mascota")
public class MascotaController {
    private final IMascotaService mascotaService;

    public MascotaController(IMascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @PostMapping
    public ResponseEntity<Mascota> crearMascota(@RequestBody MascotaDTO mascotaDTO) {
        Mascota mascotaARetornar = mascotaService.createMascota(mascotaDTO);
        if (mascotaARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(mascotaARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Mascota>> traerTodos() {
        return ResponseEntity.ok(mascotaService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mascota> buscarMascotaPorId(@PathVariable Long id) {
        Optional<Mascota> mascota = mascotaService.searchForId(id);
        if (mascota.isPresent()) {
            return ResponseEntity.ok(mascota.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarMascota(@PathVariable Long id, @RequestBody MascotaDTO mascotaDTO) {
        try {
            mascotaService.updateMascota(id, mascotaDTO);
            return ResponseEntity.ok("{\"message\": \"Mascota modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarMascota(@PathVariable Long id) {
        Optional<Mascota> mascotaOptional = mascotaService.searchForId(id);
        if (mascotaOptional.isPresent()) {
            mascotaService.deleteMascota(id);
            return ResponseEntity.ok("{\"message\": \"mascota eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"mascota no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mis-mascotas")
    public ResponseEntity<List<Mascota>> obtenerMascotasDelUsuario() {
        try {
            List<Mascota> mascotas = mascotaService.obtenerMascotasDelUsuarioAutenticado();
            return ResponseEntity.ok(mascotas);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}