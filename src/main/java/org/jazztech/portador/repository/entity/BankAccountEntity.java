package org.jazztech.portador.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Builder;

@Entity
@Table(name = "BANKACCOUNT")
public class BankAccountEntity {
    @Id
    UUID id;
    @Column(name = "bank_account")
    String bankAccount;
    @Column(name = "bank_agency")
    String bankAgency;
    @Column(name = "bank_code")
    String bankCode;

    private BankAccountEntity() {
    }

    @Builder
    public BankAccountEntity(String bankAccount, String bankAgency, String bankCode) {
        this.id = UUID.randomUUID();
        this.bankAccount = bankAccount;
        this.bankAgency = bankAgency;
        this.bankCode = bankCode;
    }

    public UUID getId() {
        return id;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getBankAgency() {
        return bankAgency;
    }

    public String getBankCode() {
        return bankCode;
    }
}
