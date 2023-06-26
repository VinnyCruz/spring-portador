package org.jazztech.portador.controller.request;

import java.util.UUID;
import lombok.Builder;

public record CardHolderRequest(
        UUID clientId,
        UUID creditAnalysisId,
        BankAccountRequest bankAccount
) {
    @Builder(toBuilder = true)
    public CardHolderRequest {
    }

    public record BankAccountRequest(String bankAccount, String bankAgency, String bankCode) {
        @Builder(toBuilder = true)
        public BankAccountRequest {
        }
    }
}
