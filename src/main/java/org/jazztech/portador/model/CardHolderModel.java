package org.jazztech.portador.model;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CardHolderModel(
        UUID clientId,
        UUID creditAnalysisId,
        String status,
        BigDecimal creditLimit,
        BankAccountModel bankAccount
) {
    @Builder(toBuilder = true)
    public CardHolderModel(UUID clientId, UUID creditAnalysisId, String status, BigDecimal creditLimit,
                           BankAccountModel bankAccount) {
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.status = status;
        this.creditLimit = creditLimit;
        this.bankAccount = bankAccount;
    }

    public CardHolderModel updateBankAccount(CardHolderModel cardHolderModel) {
        return this.toBuilder()
                .clientId(cardHolderModel.clientId())
                .creditAnalysisId(cardHolderModel.creditAnalysisId())
                .status(cardHolderModel.status())
                .creditLimit(cardHolderModel.creditLimit())
                .bankAccount(BankAccountModel.builder()
                        .bankAccount(cardHolderModel.bankAccount.bankAccount())
                        .bankAgency(cardHolderModel.bankAccount.bankAgency())
                        .bankCode(cardHolderModel.bankAccount.bankCode())
                        .build())
                .build();
    }
}
