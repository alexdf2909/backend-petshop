package com.grupo8.petshop.controller;

import com.grupo8.petshop.entity.Producto;
import com.grupo8.petshop.service.imp.RecomendacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    // Endpoint para mostrar el top de recomendaciones dinámicas
    @GetMapping("/top")
    public ResponseEntity<List<Producto>> obtenerTopRecomendados(
            @RequestParam(defaultValue = "8") int top
    ) {
        List<Producto> productos = recomendacionService.obtenerTopRecomendadosParaUsuario(top);
        return ResponseEntity.ok(productos);
    }

    // Endpoint para generar (opcional: se puede automatizar con un interceptor o evento)
    @PostMapping("/generar")
    public ResponseEntity<Void> generarRecomendaciones() {
        recomendacionService.generarRecomendacionesParaUsuario();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/populares")
    public ResponseEntity<List<Producto>> obtenerMasPopulares(
            @RequestParam(defaultValue = "8") int top
    ) {
        List<Producto> productos = recomendacionService.obtenerProductosMasPopulares(top);
        return ResponseEntity.ok(productos);
    }

    @GetMapping("/home")
    public List<Producto> obtenerRecomendacionesParaHome() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || authentication.getPrincipal().equals("anonymousUser")) {

            // No hay usuario autenticado → mostrar productos populares
            return recomendacionService.obtenerProductosMasPopulares(10);
        }

        // Usuario autenticado → generar y mostrar recomendaciones
        recomendacionService.generarRecomendacionesParaUsuario();

        List<Producto> recomendados = recomendacionService.obtenerTopRecomendadosParaUsuario(10);
        if (recomendados.isEmpty()) {
            return recomendacionService.obtenerProductosMasPopulares(10);
        }

        return recomendados;
    }

}

