package org.jazztech.portador.service.create;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jazztech.portador.apicredit.CreditApi;
import org.jazztech.portador.apicredit.dto.CreditAnalysis;
import org.jazztech.portador.controller.request.CardHolderRequest;
import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.exception.CardHolderAlreadyExistException;
import org.jazztech.portador.exception.IdsDoesntMatchException;
import org.jazztech.portador.exception.NonApprovedCreditAnalysisException;
import org.jazztech.portador.mapper.CardHolderEntityMapper;
import org.jazztech.portador.mapper.CardHolderModelMapper;
import org.jazztech.portador.mapper.CardHolderResponseMapper;
import org.jazztech.portador.model.CardHolderModel;
import org.jazztech.portador.repository.CardHolderRepository;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCardHolderService {
    private final CardHolderResponseMapper cardHolderResponseMapper;
    private final CardHolderModelMapper cardHolderModelMapper;
    private final CardHolderEntityMapper cardHolderEntityMapper;
    private final CardHolderRepository repository;
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
}
