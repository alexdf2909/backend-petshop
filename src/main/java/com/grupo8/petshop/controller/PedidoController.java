package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.entidades.PedidoDTO;
import com.grupo8.petshop.entity.Pedido;
import com.grupo8.petshop.entity.Variante;
import com.grupo8.petshop.service.IPedidoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedido")
public class PedidoController {
    private final IPedidoService pedidoService;

    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }


    @GetMapping
    public ResponseEntity<List<Pedido>> traerTodos() {
        return ResponseEntity.ok(pedidoService.searchAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscarPedidoPorId(@PathVariable Long id) {
        Optional<Pedido> pedido = pedidoService.searchForId(id);
        if (pedido.isPresent()) {
            return ResponseEntity.ok(pedido.get());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> actualizarPedido(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO) {
        try {
            pedidoService.updatePedido(id, pedidoDTO);
            return ResponseEntity.ok("{\"message\": \"Pedido modificada\"}");
        } catch (RuntimeException e) {
            return new ResponseEntity<>("{\"message\": \"" + e.getMessage() + "\"}", HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/mis-pedidos")
    public ResponseEntity<List<Pedido>> obtenerPedidosDelUsuario() {
        try {
            List<Pedido> pedidos = pedidoService.obtenerPedidosDelUsuarioAutenticado();
            return ResponseEntity.ok(pedidos);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Pedido>> getPedidosByUsuario(@PathVariable Long usuarioId) {
        List<Pedido> pedidos = pedidoService.searchByUsuario(usuarioId);
        return ResponseEntity.ok(pedidos);
    }
}
