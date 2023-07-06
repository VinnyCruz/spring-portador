package org.jazztech.portador.service.create;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
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
import org.jazztech.portador.mapper.CardHolderEntityMapperImpl;
import org.jazztech.portador.mapper.CardHolderModelMapper;
import org.jazztech.portador.mapper.CardHolderModelMapperImpl;
import org.jazztech.portador.mapper.CardHolderResponseMapper;
import org.jazztech.portador.mapper.CardHolderResponseMapperImpl;
import org.jazztech.portador.mapper.CreditCardEntityMapper;
import org.jazztech.portador.mapper.CreditCardEntityMapperImpl;
import org.jazztech.portador.mapper.CreditCardModelMapper;
import org.jazztech.portador.mapper.CreditCardModelMapperImpl;
import org.jazztech.portador.mapper.CreditCardResponseMapper;
import org.jazztech.portador.mapper.CreditCardResponseMapperImpl;
import org.jazztech.portador.model.BankAccountModel;
import org.jazztech.portador.model.CreditCardModel;
import org.jazztech.portador.repository.CardHolderRepository;
import org.jazztech.portador.repository.CreditCardRepository;
import org.jazztech.portador.repository.entity.BankAccountEntity;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.jazztech.portador.service.search.SearchCardHolderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

@ExtendWith(MockitoExtension.class)
class CreateCardHolderServiceTest {
    @InjectMocks
    private CreateCardHolderService service;
    @Mock
    private CardHolderRepository cardHolderRepository;
    @Mock
    private CreditCardRepository creditCardRepository;
    @Mock
    private CreditApi creditApi;
    @Mock
    private SearchCardHolderService searchService;

    @Spy
    private CardHolderResponseMapper cardHolderResponseMapper = new CardHolderResponseMapperImpl();
    @Spy
    private CardHolderModelMapper cardHolderModelMapper = new CardHolderModelMapperImpl();
    @Spy
    private CardHolderEntityMapper cardHolderEntityMapper = new CardHolderEntityMapperImpl();
    @Spy
    private CreditCardResponseMapper creditCardResponseMapper = new CreditCardResponseMapperImpl();
    @Spy
    private CreditCardModelMapper creditCardModelMapper = new CreditCardModelMapperImpl();
    @Spy
    private CreditCardEntityMapper creditCardEntityMapper = new CreditCardEntityMapperImpl();
    @Captor
    private ArgumentCaptor<UUID> idCaptor;
    @Captor
    private ArgumentCaptor<CardHolderEntity> cardHolderEntityCaptor;
    @Captor
    private ArgumentCaptor<CreditCardEntity> creditCardEntityCaptor;


    @Test
    void should_create_a_cardHolder_sucessfully() {
        final CardHolderRequest request = requestFactory();
        final CardHolderEntity entity = CardHolderEntity.builder()
                .clientId(request.clientId())
                .creditAnalysisId(request.creditAnalysisId())
                .bankAccount(bankAccountEntityFactory())
                .status(CardHolderEntity.Status.ACTIVE)
                .creditLimit(new BigDecimal(20000).setScale(2, RoundingMode.DOWN))
                .build();
        when(creditApi.getAnalysisById(request.creditAnalysisId())).thenReturn(CreditAnalysis.builder()
                .id(request.creditAnalysisId())
                .clientId(request.clientId())
                .approved(TRUE)
                .approvedLimit(new BigDecimal(20000).setScale(2, RoundingMode.DOWN))
                .build());
        when(cardHolderRepository.save(cardHolderEntityCaptor.capture())).thenReturn(entity);
        final CardHolderResponse cardHolder = service.createCardHolder(request);
        assertNotNull(cardHolder.id());
    }

    @Test
    void should_throw_IdsDoesntMatchException() {
        final CardHolderRequest request = requestFactory();
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
        when(cardHolderRepository.save(cardHolderEntityCaptor.capture())).thenThrow(DuplicateKeyException.class);
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

    @Test
    void should_create_a_new_credit_card() {
        final UUID cardHolderId = UUID.randomUUID();
        final CreditCardRequest creditCardRequest = CreditCardRequest.builder().limit(BigDecimal.ONE).build();
        when(creditCardRepository.save(creditCardEntityCaptor.capture())).thenReturn(cardEntityFactory());
        when(searchService.getCardHolderById(idCaptor.capture())).thenReturn(responseFactory());
        when(searchService.getCreditCardsByCardHolderId(idCaptor.capture())).thenReturn(List.of(cardEntityFactory(), cardEntityFactory()));

        final CreditCardResponse response = service.createCreditCard(cardHolderId, creditCardRequest);

        assertNotNull(response.id());
        assertNotNull(response.cardNumber());
        assertNotNull(response.cvv());
    }

    @Test
    void should_throw_InactiveCardHolderException() {
        final UUID cardHolderId = UUID.randomUUID();
        final CreditCardRequest creditCardRequest = CreditCardRequest.builder().limit(BigDecimal.ONE).build();
        when(searchService.getCardHolderById(idCaptor.capture())).thenReturn(inactiveResponseFactory());
        when(searchService.getCreditCardsByCardHolderId(idCaptor.capture())).thenReturn(List.of(cardEntityFactory(), cardEntityFactory()));

        assertThrows(InactiveCardHolderException.class, () -> service.createCreditCard(cardHolderId, creditCardRequest));
    }

    @Test
    void should_throw_UnavailableCreditLimitException() {
        final UUID cardHolderId = UUID.randomUUID();
        final CreditCardRequest creditCardRequest = CreditCardRequest.builder().limit(BigDecimal.valueOf(20000)).build();
        when(searchService.getCardHolderById(idCaptor.capture())).thenReturn(responseFactory());
        when(searchService.getCreditCardsByCardHolderId(idCaptor.capture())).thenReturn(List.of(cardEntityFactory(), cardEntityFactory()));

        assertThrows(UnavailableCreditLimitException.class, () -> service.createCreditCard(cardHolderId, creditCardRequest));
    }

    public CardHolderRequest requestFactory() {
        return CardHolderRequest.builder()
                .creditAnalysisId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .bankAccount(bankAccountRequestFactory())
                .build();
    }

    public CardHolderResponse responseFactory() {
        return CardHolderResponse.builder()
                .id(UUID.randomUUID())
                .status(CardHolderResponse.Status.ACTIVE)
                .creditLimit(BigDecimal.TEN)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public CardHolderResponse inactiveResponseFactory() {
        return CardHolderResponse.builder()
                .id(UUID.randomUUID())
                .status(CardHolderResponse.Status.INACTIVE)
                .creditLimit(BigDecimal.TEN)
                .createdAt(LocalDateTime.now())
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

    public CreditCardEntity cardEntityFactory() {
        return CreditCardEntity.builder()
                .cardHolderId(UUID.randomUUID())
                .cardNumber("5618 5634 9958 1910")
                .cvv(759)
                .limit(BigDecimal.ONE)
                .dueDate(LocalDate.now())
                .build();
    }

}