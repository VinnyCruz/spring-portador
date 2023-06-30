package org.jazztech.portador.controller.response;

import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

public record CreditCardResponse(
        UUID id,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate
) {
    @Builder(toBuilder = true)
    public CreditCardResponse(UUID id, String cardNumber, Integer cvv, LocalDate dueDate) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
    }
}
