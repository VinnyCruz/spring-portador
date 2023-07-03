package org.jazztech.portador.service.create;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import org.jazztech.portador.apicredit.CreditApi;
import org.jazztech.portador.apicredit.dto.CreditAnalysis;
import org.jazztech.portador.controller.request.CardHolderRequest;
import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.exception.CardHolderAlreadyExistException;
import org.jazztech.portador.exception.IdsDoesntMatchException;
import org.jazztech.portador.exception.InactiveCardHolderException;
import org.jazztech.portador.exception.NonApprovedCreditAnalysisException;
import org.jazztech.portador.mapper.CardHolderEntityMapper;
import org.jazztech.portador.mapper.CardHolderEntityMapperImpl;
import org.jazztech.portador.mapper.CardHolderModelMapper;
import org.jazztech.portador.mapper.CardHolderModelMapperImpl;
import org.jazztech.portador.mapper.CardHolderResponseMapper;
import org.jazztech.portador.mapper.CardHolderResponseMapperImpl;
import org.jazztech.portador.repository.CardHolderRepository;
import org.jazztech.portador.repository.entity.BankAccountEntity;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.server.ResponseStatusException;

@ExtendWith(MockitoExtension.class)
class CreateCardHolderServiceTest {
    @InjectMocks
    private CreateCardHolderService service;
    @Mock
    private CardHolderRepository repository;
    @Mock
    private CreditApi creditApi;
    @Spy
    private CardHolderResponseMapper cardHolderResponseMapper = new CardHolderResponseMapperImpl();
    @Spy
    private CardHolderModelMapper cardHolderModelMapper = new CardHolderModelMapperImpl();
    @Spy
    private CardHolderEntityMapper cardHolderEntityMapper = new CardHolderEntityMapperImpl();
    @Captor
    private ArgumentCaptor<UUID> idCaptor;
    @Captor
    private ArgumentCaptor<CardHolderEntity> cardHolderEntityCaptor;

    @Test
    void should_create_a_cardHolder_sucessfully() {
        final CardHolderRequest request = requestFactory();
        final CardHolderEntity entity = CardHolderEntity.builder()
                .clientId(request.clientId())
                .creditAnalysisId(request.creditAnalysisId())
                .bankAccount(bankAccountEntityFactory()).status(CardHolderEntity.Status.ACTIVE)
                .creditLimit(new BigDecimal(20000).setScale(2, RoundingMode.DOWN))
                .build();
        when(creditApi.getAnalysisById(request.creditAnalysisId())).thenReturn(CreditAnalysis.builder()
                .id(request.creditAnalysisId())
                .clientId(request.clientId())
                .approved(TRUE)
                .approvedLimit(new BigDecimal(20000).setScale(2, RoundingMode.DOWN))
                .build());
        when(repository.save(cardHolderEntityCaptor.capture())).thenReturn(entity);
        final CardHolderResponse cardHolder = service.createCardHolder(request);
        assertNotNull(cardHolder.id());
    }

    @Test
    void should_throw_IdsDoesntMatchException() {
        final CardHolderRequest request = requestFactory();
        final CardHolderEntity entity = CardHolderEntity.builder()
                .clientId(request.clientId())
                .creditAnalysisId(request.creditAnalysisId())
                .bankAccount(bankAccountEntityFactory()).status(CardHolderEntity.Status.ACTIVE)
                .creditLimit(new BigDecimal(20000).setScale(2, RoundingMode.DOWN))
                .build();
        when(creditApi.getAnalysisById(request.creditAnalysisId())).thenReturn(CreditAnalysis.builder()
                .id(request.creditAnalysisId())
                .clientId(UUID.randomUUID())
                .approved(TRUE)
                .approvedLimit(new BigDecimal(20000).setScale(2, RoundingMode.DOWN))
                .build());
        assertThrows(IdsDoesntMatchException.class, () -> service.createCardHolder(request));

    }

    @Test
    void should_throw_CardHolderAlreadyExistException() {
        final CardHolderEntity entity = CardHolderEntity.builder()
                .clientId(UUID.randomUUID())
                .creditAnalysisId(UUID.randomUUID())
                .bankAccount(bankAccountEntityFactory()).status(CardHolderEntity.Status.ACTIVE)
                .creditLimit(new BigDecimal(20000).setScale(2, RoundingMode.DOWN))
                .build();
        when(repository.save(cardHolderEntityCaptor.capture())).thenThrow(DuplicateKeyException.class);
        assertThrows(CardHolderAlreadyExistException.class,  () -> service.saveCardHolder(entity));
    }

    @Test
    void should_throw_NonApprovedCreditAnalysisException() {
        final UUID analysisId = UUID.randomUUID();
        when(creditApi.getAnalysisById(idCaptor.capture())).thenReturn(
                CreditAnalysis.builder()
                        .id(analysisId)
                        .clientId(UUID.randomUUID())
                        .approved(FALSE)
                        .approvedLimit(BigDecimal.ZERO)
                        .build()
        );
        assertThrows(NonApprovedCreditAnalysisException.class,  () -> service.checkAnalysis(analysisId));
    }

    public CardHolderRequest requestFactory() {
        return CardHolderRequest.builder()
                .creditAnalysisId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .bankAccount(bankAccountRequestFactory())
                .build();
    }

    public CardHolderRequest.BankAccountRequest bankAccountRequestFactory() {
        return CardHolderRequest.BankAccountRequest.builder()
                .bankAccount("0795")
                .bankAgency("0001")
                .bankCode("777")
                .build();
    }

    public BankAccountEntity bankAccountEntityFactory() {
        return BankAccountEntity.builder()
                .bankAccount("0795")
                .bankAgency("0001")
                .bankCode("777")
                .build();
    }
}