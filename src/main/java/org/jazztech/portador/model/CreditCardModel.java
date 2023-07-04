package org.jazztech.portador.model;

import java.util.Date;
import lombok.Builder;
import org.jazztech.portador.repository.entity.CardHolderEntity;

public record CreditCardModel(
        CardHolderEntity cardHolder,
        String cardNumber,
        Integer cvv,
        Date dueDate
) {
    @Builder(toBuilder = true)
    public CreditCardModel(CardHolderEntity cardHolder, String cardNumber, Integer cvv, Date dueDate) {
        this.cardHolder = cardHolder;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
    }
}
