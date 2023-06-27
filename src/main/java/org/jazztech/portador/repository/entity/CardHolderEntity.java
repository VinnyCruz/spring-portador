package org.jazztech.portador.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "CARDHOLDER")
public class CardHolderEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    @Column(name = "client_id")
    UUID clientId;

    @Column(name = "credit_analysis_id")
    UUID creditAnalysisId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bank_account_id", referencedColumnName = "id")
    BankAccountEntity bankAccount;

    String status;

    @Column(name = "credit_limit")
    BigDecimal creditLimit;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createdAt;

    private CardHolderEntity() {
    }

    @Builder
    public CardHolderEntity(UUID clientId, UUID creditAnalysisId, BankAccountEntity bankAccount, String status, BigDecimal creditLimit) {
        this.id = UUID.randomUUID();
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.bankAccount = bankAccount;
        this.status = status;
        this.creditLimit = creditLimit;
    }

    public UUID getId() {
        return id;
    }

    public UUID getClientId() {
        return clientId;
    }

    public UUID getCreditAnalysisId() {
        return creditAnalysisId;
    }

    public BankAccountEntity getBankAccount() {
        return bankAccount;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "CardHolderEntity{"
                + "id=" + id
                + ", clientId=" + clientId
                + ", creditAnalysisId=" + creditAnalysisId
                + ", bankAccountId=" + bankAccount
                + ", status='" + status + '\''
                + ", creditLimit=" + creditLimit
                + ", createdAt=" + createdAt
                + '}';
    }
}
