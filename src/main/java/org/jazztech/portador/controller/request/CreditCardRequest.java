package org.jazztech.portador.controller.request;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CreditCardRequest(
        UUID cardHolderId,
        BigDecimal limit
) {
    @Builder(toBuilder = true)
    public CreditCardRequest {
    }
}
