package org.jazztech.portador.controller.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

public record CardHolderResponse(
        UUID id,
        String status,
        BigDecimal creditLimit,
        LocalDateTime createdAt
) {
    @Builder(toBuilder = true)
    public CardHolderResponse(UUID id, String status, BigDecimal creditLimit, LocalDateTime createdAt) {
        this.id = id;
        this.status = status;
        this.creditLimit = creditLimit;
        this.createdAt = createdAt;
    }
}
