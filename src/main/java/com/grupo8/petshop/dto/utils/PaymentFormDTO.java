package com.grupo8.petshop.dto.utils;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

// PaymentFormDTO.java
@Getter
@Setter
public class PaymentFormDTO {
    private String token;
    private String issuer_id;
    private String payment_method_id;
    private BigDecimal transaction_amount;
    private Integer installments;
    private Payer payer;

    // Getters y setters
    @Getter @Setter
    public static class Payer {
        private String email;
        private Identification identification;

        // Getters y setters
        @Getter @Setter
        public static class Identification {
            private String type;
            private String number;

            // Getters y setters
        }
    }
}