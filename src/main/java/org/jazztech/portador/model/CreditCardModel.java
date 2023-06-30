package org.jazztech.portador.model;

import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;
import lombok.Builder;

public record CreditCardModel(
        UUID cardHolderId,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate,
        @Positive
        BigDecimal limit
) {
    private static final byte CARD_NUMBER_FIRST_DIGIT = 5;
    private static final byte CARD_NUMBER_LENGTH = 15;
    private static final byte CARD_NUMBER_FORMATTING = 4;
    private static final byte DUE_DATE_IN_YEARS = 2;


    @Builder(toBuilder = true)
    public CreditCardModel(UUID cardHolderId, String cardNumber, Integer cvv, LocalDate dueDate, BigDecimal limit) {
        this.cardHolderId = cardHolderId;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
        this.limit = limit;
    }


    private String generateCardNumber() {
        final StringBuilder numero = new StringBuilder();
        final Random random = new Random();

        numero.append(CARD_NUMBER_FIRST_DIGIT);

        for (int i = 1; i <= CARD_NUMBER_LENGTH; i++) {
            if (i % CARD_NUMBER_FORMATTING == 0) {
                numero.append(" ");
            }
            final int digito = random.nextInt(10);
            numero.append(digito);
        }

        return numero.toString();
    }

    private int generateCVV() {
        final Random random = new Random();
        final int cvv = 100 + random.nextInt(900);

        return cvv;
    }

    private LocalDate generateDueDate() {
        final LocalDate now = LocalDate.now();
        return now.plusYears(DUE_DATE_IN_YEARS);
    }

    public CreditCardModel updateCreditCardInformation(UUID cardHolderId, CreditCardModel creditCardModel) {
        return this.toBuilder()
                .cardHolderId(cardHolderId)
                .cardNumber(generateCardNumber())
                .cvv(generateCVV())
                .dueDate(generateDueDate())
                .limit(creditCardModel.limit)
                .build();
    }
}
