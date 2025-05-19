package com.grupo8.petshop.controller;

import com.grupo8.petshop.dto.utils.CartItemDTO;
import com.grupo8.petshop.dto.utils.PagoRequestDTO;
import com.grupo8.petshop.dto.utils.PaymentFormDTO;
import com.grupo8.petshop.service.imp.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pago")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/preferencia")
    public ResponseEntity<String> crearPreferencia(@RequestBody List<CartItemDTO> carrito) {
        try {
            String preferenceId = paymentService.crearPreferencia(carrito);
            return ResponseEntity.ok(preferenceId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear preferencia: " + e.getMessage());
        }
    }

    @PostMapping("/procesar")
    public ResponseEntity<String> procesarPago(@RequestBody PaymentFormDTO form) {
        try {
            String estado = paymentService.procesarPago(form);
            return ResponseEntity.ok("Pago " + estado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar el pago: " + e.getMessage());
        }
    }

    @PostMapping("/completar")
    public ResponseEntity<String> procesarPagoCompleto(@RequestBody PagoRequestDTO dto) {
        try {
            String mensaje = paymentService.procesarPedidoPago(dto);
            return ResponseEntity.ok(mensaje);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

}

