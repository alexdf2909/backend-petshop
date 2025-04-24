package com.grupo8.petshop.service.imp;

import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
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
    @Autowired
    private IVarianteRepository varianteRepository;
    private final PreferenceClient preferenceClient;

    public String crearPreferencia(List<CartItemDTO> carrito) throws Exception {

        List<PreferenceItemRequest> items = carrito.stream().map(item -> {
            Variante variante = varianteRepository.findById(item.getVarianteId())
                    .orElseThrow(() -> new RuntimeException("Variante no encontrada: " + item.getVarianteId()));

            return PreferenceItemRequest.builder()
                    .id(variante.getVarianteId().toString())
                    .title(variante.getProducto().getNombre())
                    .quantity(item.getCantidad())
                    .unitPrice(BigDecimal.valueOf(variante.getPrecioOferta()))
                    .currencyId("PER") // o "USD", según corresponda
                    .build();
        }).collect(Collectors.toList());

        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items)
                .build();

        Preference preference = preferenceClient.create(preferenceRequest);

        return preference.getId();
    }
}
