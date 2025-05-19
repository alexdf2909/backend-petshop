package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.utils.PagoRequestDTO;
import com.grupo8.petshop.dto.utils.PaymentFormDTO;
import com.grupo8.petshop.entity.*;
import com.grupo8.petshop.repository.*;
import com.grupo8.petshop.util.EstadoPedido;
import com.grupo8.petshop.util.MetodoPago;
import com.grupo8.petshop.util.ModoEntrega;
import com.grupo8.petshop.util.ProgresoEntrega;
import com.mercadopago.client.common.IdentificationRequest;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.payment.PaymentCreateRequest;
import com.mercadopago.client.payment.PaymentPayerRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import com.grupo8.petshop.dto.utils.CartItemDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IVarianteRepository varianteRepository;
    private final PreferenceClient preferenceClient;
    private final IUsuarioRepository usuarioRepository;
    private final ILoteRepository loteRepository;
    private final IPedidoRepository pedidoRepository;
    private final IDetallePedidoRepository detallePedidoRepository;

    public String crearPreferencia(List<CartItemDTO> carrito) throws Exception {

        List<PreferenceItemRequest> items = carrito.stream().map(item -> {
            Variante variante = varianteRepository.findById(item.getVarianteId())
                    .orElseThrow(() -> new IllegalArgumentException("Variante no encontrada: " + item.getVarianteId()));

            return PreferenceItemRequest.builder()
                    .id(variante.getVarianteId().toString())
                    .title(variante.getProducto().getNombre() + " " + variante.getPeso() + " " + variante.getColor() + " " + variante.getTalla())
                    .quantity(item.getCantidad())
                    .unitPrice(BigDecimal.valueOf(variante.getPrecioOferta()))
                    .currencyId("PEN") // o "USD", según corresponda
                    .build();
        }).collect(Collectors.toList());

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        Preference preference = preferenceClient.create(preferenceRequest);

        return preference.getId();
    }
    public String procesarPago(PaymentFormDTO request) throws Exception {
        PaymentClient client = new PaymentClient();

        PaymentCreateRequest paymentCreateRequest =
                PaymentCreateRequest.builder()
                        .transactionAmount(request.getTransaction_amount())
                        .token(request.getToken())
                        .installments(request.getInstallments())
                        .paymentMethodId(request.getPayment_method_id())
                        .issuerId(request.getIssuer_id()) // si es requerido
                        .payer(
                                PaymentPayerRequest.builder()
                                        .email(request.getPayer().getEmail())
                                        .identification(
                                                IdentificationRequest.builder()
                                                        .type(request.getPayer().getIdentification().getType())
                                                        .number(request.getPayer().getIdentification().getNumber())
                                                        .build())
                                        .build())
                        .build();

        Payment payment = client.create(paymentCreateRequest);

        return payment.getStatus(); // o lo que desees retornar
    }
    @Transactional
    public String procesarPedidoPago(PagoRequestDTO request) throws Exception {
        // 1. Validar monto
        float totalCalculado = 0f;
        for (CartItemDTO item : request.getCarrito()) {
            Variante variante = varianteRepository.findById(item.getVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante no encontrada"));
            totalCalculado += item.getCantidad() * variante.getPrecioOferta();
        }

        if (Float.compare(totalCalculado, request.getMontoTotal()) != 0) {
            throw new RuntimeException("El monto total no coincide con el carrito");
        }

        // 2. Validar dirección o retiro
        if (request.getModoEntrega().equals("ENVIO") && (request.getDireccionEnvio() == null || request.getDireccionEnvio().isBlank())) {
            throw new RuntimeException("Debe ingresar dirección si seleccionó envío");
        }

        // 3. Procesar pago
        String estado = procesarPago(request.getPago());

        if (!"approved".equalsIgnoreCase(estado)) {
            throw new RuntimeException("Pago no aprobado. Estado: " + estado);
        }

        // 4. Registrar Pedido
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String metodoPagoMP = request.getPago().getPayment_method_id(); // ej. "visa", "master"
        MetodoPago metodo;

        if (metodoPagoMP.equalsIgnoreCase("visa") || metodoPagoMP.equalsIgnoreCase("master") || metodoPagoMP.equalsIgnoreCase("amex")) {
            metodo = MetodoPago.CREDITO;
        } else if (metodoPagoMP.equalsIgnoreCase("debvisa") || metodoPagoMP.equalsIgnoreCase("masterdebit")) {
            metodo = MetodoPago.DEBITO;
        } else {
            throw new RuntimeException("Método de pago no reconocido: " + metodoPagoMP);
        }

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .fechaRegistro(LocalDateTime.now())
                .montoTotal(request.getMontoTotal())
                .direccionEnvio(request.getDireccionEnvio())
                .modoEntrega(ModoEntrega.valueOf(request.getModoEntrega()))
                .metodoPago(metodo)
                .estadoPedido(EstadoPedido.PENDIENTE)
                .progresoEntrega(ProgresoEntrega.EN_ESPERA)
                .tiempoEstimadoEntrega(null) // o lo que corresponda
                .build();

        pedidoRepository.save(pedido);

        // 5. Registrar detalle y actualizar stock
        for (CartItemDTO item : request.getCarrito()) {
            Variante variante = varianteRepository.findById(item.getVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante no encontrada"));

            DetallePedido detalle = DetallePedido.builder()
                    .pedido(pedido)
                    .producto(variante.getProducto())
                    .variante(variante)
                    .cantidad(item.getCantidad())
                    .precioUnitario(variante.getPrecioOferta())
                    .subtotal(item.getCantidad() * variante.getPrecioOferta())
                    .build();

            detallePedidoRepository.save(detalle);

            int cantidadNecesaria = item.getCantidad();
            List<Lote> lotesWithVariante = loteRepository.findByVarianteAndIsDeletedFalse(variante);
            // Marcar todas las lotes asociadas como eliminadas lógicamente
            for (Lote lote : lotesWithVariante) {
                if(lote.getFechaVencimiento().isAfter(LocalDate.now()) && lote.getStock() > 0){
                    if(cantidadNecesaria > lote.getStock()){
                        cantidadNecesaria -= lote.getStock();
                        lote.setStock(0);
                    }else{
                        lote.setStock(lote.getStock() - cantidadNecesaria);
                        break;
                    }
                }
            }
            // Guardar los cambios en los lotes
            loteRepository.saveAll(lotesWithVariante);
        }

        return "Pago exitoso y pedido registrado con ID: " + pedido.getPedidoId();
    }

}
