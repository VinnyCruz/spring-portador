package org.jazztech.portador.service.create;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jazztech.portador.apicredit.CreditApi;
import org.jazztech.portador.apicredit.dto.CreditAnalysis;
import org.jazztech.portador.controller.request.CardHolderRequest;
import org.jazztech.portador.controller.request.CreditCardRequest;
import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.controller.response.CreditCardResponse;
import org.jazztech.portador.exception.CardHolderAlreadyExistException;
import org.jazztech.portador.exception.IdsDoesntMatchException;
import org.jazztech.portador.exception.InactiveCardHolderException;
import org.jazztech.portador.exception.NonApprovedCreditAnalysisException;
import org.jazztech.portador.exception.UnavailableCreditLimitException;
import org.jazztech.portador.mapper.CardHolderEntityMapper;
import org.jazztech.portador.mapper.CardHolderModelMapper;
import org.jazztech.portador.mapper.CardHolderResponseMapper;
import org.jazztech.portador.mapper.CreditCardEntityMapper;
import org.jazztech.portador.mapper.CreditCardModelMapper;
import org.jazztech.portador.mapper.CreditCardResponseMapper;
import org.jazztech.portador.model.CardHolderModel;
import org.jazztech.portador.model.CreditCardModel;
import org.jazztech.portador.repository.CardHolderRepository;
import org.jazztech.portador.repository.CreditCardRepository;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.jazztech.portador.service.search.SearchCardHolderService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCardHolderService {
    private final SearchCardHolderService searchCardHolder;
    private final CardHolderResponseMapper cardHolderResponseMapper;
    private final CardHolderModelMapper cardHolderModelMapper;
    private final CardHolderEntityMapper cardHolderEntityMapper;
    private final CreditCardResponseMapper creditCardResponseMapper;
    private final CreditCardModelMapper creditCardModelMapper;
    private final CreditCardEntityMapper creditCardEntityMapper;
    private final CardHolderRepository repository;
    private final CreditCardRepository creditCardRepository;
    private final CreditApi creditApi;


    public CardHolderResponse createCardHolder(CardHolderRequest cardHolderRequest) {
        final CardHolderModel cardHolder = cardHolderModelMapper.from(cardHolderRequest);
        final CreditAnalysis creditAnalysis = checkAnalysis(cardHolder.creditAnalysisId());
        if (!cardHolder.clientId().equals(creditAnalysis.clientId())) {
            throw new IdsDoesntMatchException("A análise de crédito não corresponde ao cliente informado");
        }
        final CardHolderModel cardHolderApproved = cardHolder.toBuilder().status("ACTIVE").creditLimit(creditAnalysis.approvedLimit()).build();
        final CardHolderModel cardHolderBankUpdated = cardHolderApproved.updateBankAccount(cardHolderApproved);
        final CardHolderEntity cardHolderEntity = cardHolderEntityMapper.from(cardHolderBankUpdated);
        final CardHolderEntity cardHolderSaved = saveCardHolder(cardHolderEntity);
        return cardHolderResponseMapper.from(cardHolderSaved);
    }

    public CardHolderEntity saveCardHolder(CardHolderEntity cardHolderEntity) {
        final CardHolderEntity cardHolderSaved;
        try {
            cardHolderSaved = repository.save(cardHolderEntity);
        } catch (DataIntegrityViolationException exception) {
            throw new CardHolderAlreadyExistException("Portador já cadastrado no sistema.");
        }
        return cardHolderSaved;
    }

    public CreditAnalysis checkAnalysis(UUID analysisId) {
        final CreditAnalysis creditAnalysis = creditApi.getAnalysisById(analysisId);
        if (!creditAnalysis.approved()) {
            throw new NonApprovedCreditAnalysisException("A análise de crédito informada não foi aprovada.");
        }
        return creditAnalysis;
    }

    public CreditCardResponse createCreditCard(UUID cardHolderId, CreditCardRequest creditCardRequest) {
        final CreditCardModel newCreditCard = validateNewCreditCard(cardHolderId, creditCardRequest);
        final CreditCardEntity creditCardEntity = creditCardEntityMapper.from(newCreditCard);
        final CreditCardEntity creditCardSaved = creditCardRepository.save(creditCardEntity);
        return creditCardResponseMapper.from(creditCardSaved);
    }

    private CreditCardModel validateNewCreditCard(UUID cardHolderId, CreditCardRequest creditCardRequest) {
        final CreditCardModel creditCard = creditCardModelMapper.from(creditCardRequest);
        final CardHolderResponse cardHolder = searchCardHolder.getCardHolderById(cardHolderId);
        final List<CreditCardModel> cardHolderCreditCards = searchCardHolder.getCreditCardsByCardHolderId(cardHolderId);
        final BigDecimal usedLimit = cardHolderCreditCards.stream().map(CreditCardModel::limit).reduce(BigDecimal.ZERO, BigDecimal::add);
        final BigDecimal remainingLimit = cardHolder.creditLimit().subtract(usedLimit);
        if (!cardHolder.status().equals("ACTIVE")) {
            throw new InactiveCardHolderException("O portador não está ativo. Não é possível solicitar um novo cartão de crédito.");
        } else if (creditCard.limit().compareTo(remainingLimit) > 0) {
            throw new UnavailableCreditLimitException("O limite solicitado para o cartão é maior que o limite disponível para o portador.");
        }
        return creditCard.updateCreditCardInformation(creditCard);
    }
}


