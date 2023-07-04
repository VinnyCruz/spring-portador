package org.jazztech.portador.apicredit.dto;


import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CreditAnalysis(
        UUID id,
        UUID clientId,
        Boolean approved,
        BigDecimal approvedLimit
) {
    @Builder(toBuilder = true)
    public CreditAnalysis(UUID id, UUID clientId, Boolean approved, BigDecimal approvedLimit) {
        this.id = id;
        this.clientId = clientId;
        this.approved = approved;
        this.approvedLimit = approvedLimit;
    }
}
