package com.grupo8.petshop.service.imp;

import com.grupo8.petshop.dto.utils.PaymentFormDTO;
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
import com.grupo8.petshop.entity.Variante;
import com.grupo8.petshop.repository.IVarianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final IVarianteRepository varianteRepository;
    private final PreferenceClient preferenceClient;

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
}
