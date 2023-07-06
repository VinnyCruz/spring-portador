package org.jazztech.portador.service.update;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jazztech.portador.controller.request.LimitUpdateRequest;
import org.jazztech.portador.controller.response.LimitUpdateResponse;
import org.jazztech.portador.exception.InsufficientCreditLimitException;
import org.jazztech.portador.mapper.LimitUpdateResponseMapper;
import org.jazztech.portador.repository.CardHolderRepository;
import org.jazztech.portador.repository.CreditCardRepository;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.jazztech.portador.service.search.SearchCardHolderService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UpdateCreditCardService {
    private final SearchCardHolderService search;
    private final CardHolderRepository cardHolderRepository;
    private final CreditCardRepository creditCardRepository;
    private final LimitUpdateResponseMapper limitUpdateResponseMapper;

    public LimitUpdateResponse updateCreditCardLimit(UUID cardHolderId, UUID id, LimitUpdateRequest limitUpdateRequest) {
        final CardHolderEntity cardHolder = cardHolderRepository.findById(cardHolderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Portador não encontrado"));
        final List<CreditCardEntity> cardHolderCreditCards = search.getCreditCardsByCardHolderId(cardHolderId);
        final BigDecimal usedLimit = cardHolderCreditCards.stream().map(CreditCardEntity::getLimit).reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal oldRemainingLimit = cardHolder.getCreditLimit().subtract(usedLimit);
        final BigDecimal newRemainingLimit = oldRemainingLimit.subtract(limitUpdateRequest.newLimit());
        if (newRemainingLimit.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientCreditLimitException("O limite solicitado é maior que o seu limite de crédito disponível.");
        }
        final CreditCardEntity creditCard = search.getCreditCardEntityById(cardHolderId, id);
        creditCardRepository.save(creditCard.toBuilder()
                .limit(limitUpdateRequest.newLimit())
                .build());
        return limitUpdateResponseMapper.from(limitUpdateRequest);
    }
}


