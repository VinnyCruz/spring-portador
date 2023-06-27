package org.jazztech.portador.repository.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.UUID;
import lombok.Builder;
import org.springframework.data.annotation.Immutable;

@Entity
@Table(name = "CREDITCARD")
@Immutable
public class CreditCardEntity {
    @Id
    UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_holder_id", referencedColumnName = "id")
    CardHolderEntity cardHolder;

    @Column(name = "card_number")
    String cardNumber;

    Integer cvv;

    @Column(name = "due_date")
    Date dueDate;

    public CreditCardEntity() {
    }

    @Builder(toBuilder = true)
    public CreditCardEntity(CardHolderEntity cardHolder, String cardNumber, Integer cvv, Date dueDate) {
        this.id = UUID.randomUUID();
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
    }




    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CardHolderEntity getCardHolder() {
        return cardHolder;
    }

    public void setCardHolderId(CardHolderEntity cardHolder) {
        this.cardHolder = cardHolder;
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

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
