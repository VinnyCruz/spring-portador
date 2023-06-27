package org.jazztech.portador.model;

import lombok.Builder;

@SuppressWarnings("checkstyle:MethodParamPad")
public record BankAccountModel(
        String bankAccount,
        String bankAgency,
        String bankCode
) {
    @Builder(toBuilder = true)
    public BankAccountModel(String bankAccount, String bankAgency, String bankCode) {
        this.bankAccount = bankAccount;
        this.bankAgency = bankAgency;
        this.bankCode = bankCode;
    }
}
