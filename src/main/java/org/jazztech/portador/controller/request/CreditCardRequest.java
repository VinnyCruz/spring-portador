package org.jazztech.portador.controller.request;

import java.math.BigDecimal;
import lombok.Builder;

public record CreditCardRequest(
        BigDecimal limit
) {
    @Builder(toBuilder = true)
    public CreditCardRequest {
    }
}
