package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.CompraDTO;
import com.grupo8.petshop.entity.Compra;
import com.grupo8.petshop.service.ICompraService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compra")
public class CompraController {
    private final ICompraService compraService;

    public CompraController(ICompraService compraService) {
        this.compraService = compraService;
    }

    @PostMapping
    public ResponseEntity<Compra> crearCompra(@RequestBody CompraDTO compraDTO) {
        Compra compraARetornar = compraService.createCompra(compraDTO);
        if (compraARetornar == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(compraARetornar);
        }
    }

    @GetMapping
    public ResponseEntity<List<Compra>> traerTodos() {
        return ResponseEntity.ok(compraService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Compra> buscarCompraPorId(@PathVariable Long id) {
        Optional<Compra> compra = compraService.searchForId(id);
        if (compra.isPresent()) {
            return ResponseEntity.ok(compra.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarCompra(@PathVariable Long id, @RequestBody CompraDTO compraDTO) {
        try {
            compraService.updateCompra(id, compraDTO);
            return ResponseEntity.ok("{\"message\": \"Compra modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> borrarCompra(@PathVariable Long id) {
        Optional<Compra> compraOptional = compraService.searchForId(id);
        if (compraOptional.isPresent()) {
            compraService.deleteCompra(id);
            return ResponseEntity.ok("{\"message\": \"compra eliminada\"}");
        } else {
            return new ResponseEntity<>("{\"message\": \"compra no encontrada\"}", HttpStatus.NOT_FOUND);
        }
    }
}
