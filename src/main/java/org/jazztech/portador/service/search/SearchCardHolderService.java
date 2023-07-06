package org.jazztech.portador.service.search;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jazztech.portador.apicredit.CreditApi;
import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.exception.IdsDoesntMatchException;
import org.jazztech.portador.mapper.CardHolderResponseMapper;
import org.jazztech.portador.repository.CardHolderRepository;
import org.jazztech.portador.repository.CreditCardRepository;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SearchCardHolderService {
    private final CardHolderResponseMapper cardHolderResponseMapper;
    private final CardHolderRepository repository;
    private final CreditCardRepository creditCardRepository;
    private final CreditApi creditApi;

    public List<CardHolderResponse> getCardHoldersBy(CardHolderEntity.Status status) {
        final List<CardHolderEntity> cardHolders;
        if (status != null) {
            cardHolders = repository.findByStatus(status);
        } else {
            cardHolders = repository.findAll();
        }
        return cardHolders
                .stream()
                .map(cardHolderResponseMapper::from)
                .collect(Collectors.toList());
    }

    public CardHolderResponse getCardHolderById(UUID id) {
        final CardHolderEntity cardHolderEntity = repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Portador não encontrado"));
        return cardHolderResponseMapper.from(cardHolderEntity);
    }

    public List<CreditCardEntity> getCreditCardsByCardHolderId(UUID cardHolderId) {
        return creditCardRepository.findAllByCardHolderId(cardHolderId);
    }

    public CreditCardEntity getCreditCardById(UUID cardHolderId, UUID id) {
        getCardHolderById(cardHolderId);
        final CreditCardEntity creditCard = creditCardRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Cartão de crédito não encontrado"));
        if (!creditCard.getCardHolderId().equals(cardHolderId)) {
            throw new IdsDoesntMatchException("O cartão de crédito não pertence ao portador informado");
        }
        return creditCard;
    }
}
