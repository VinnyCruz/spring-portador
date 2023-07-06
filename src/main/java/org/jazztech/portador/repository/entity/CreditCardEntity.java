package org.jazztech.portador.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

@Entity
@Table(name = "CREDITCARD")
public class CreditCardEntity {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    UUID cardHolderId;

    @Column(name = "card_number")
    String cardNumber;

    Integer cvv;

    @Column(name = "due_date")
    LocalDate dueDate;
    @Column(name = "card_limit")
    BigDecimal limit;

    private CreditCardEntity() {
    }

    @Builder(toBuilder = true)
    public CreditCardEntity(UUID cardHolderId, String cardNumber, Integer cvv, LocalDate dueDate, BigDecimal limit) {
        this.cardHolderId = cardHolderId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
        this.limit = limit;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCardHolderId() {
        return cardHolderId;
    }

    public void setCardHolderId(UUID cardHolderId) {
        this.cardHolderId = cardHolderId;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    @Override
    public String toString() {
        return "CreditCardEntity{"
                + "id=" + id
                + ", cardHolderId=" + cardHolderId
                + ", cardNumber='" + cardNumber + '\''
                + ", cvv=" + cvv
                + ", dueDate=" + dueDate
                + ", limit=" + limit
                + '}';
    }
}
