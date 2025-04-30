package com.grupo8.petshop.controller;

import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.service.imp.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/recomendaciones")
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    @GetMapping("/mascota/{id}")
    public ResponseEntity<List<Producto>> recomendarPorMascota(@PathVariable Long id) {
        List<Producto> productos = recomendacionService.recomendarProductosPorMascota(id);
        return ResponseEntity.ok(productos);
    }
}

