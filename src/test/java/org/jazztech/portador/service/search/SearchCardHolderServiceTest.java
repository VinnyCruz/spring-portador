package org.jazztech.portador.service.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.jazztech.portador.apicredit.CreditApi;
import org.jazztech.portador.controller.response.CardHolderResponse;
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

@ExtendWith(MockitoExtension.class)
class SearchCardHolderServiceTest {
    @InjectMocks
    private SearchCardHolderService service;
    @Mock
    private CardHolderRepository repository;
    @Mock
    private CreditApi creditApi;
    @Spy
    private CardHolderResponseMapper cardHolderResponseMapper = new CardHolderResponseMapperImpl();
    @Captor
    private ArgumentCaptor<UUID> idCaptor;
    @Captor
    private ArgumentCaptor<CardHolderEntity> cardHolderEntityCaptor;

    @Test
    void should_return_a_list_of_active_card_holders() {
        when(repository.findByStatus(CardHolderEntity.Status.ACTIVE)).thenReturn(List.of(entityFactory(),entityFactory()));
        List<CardHolderResponse> responses = service.getCardHoldersBy(CardHolderEntity.Status.ACTIVE);
        assertEquals(CardHolderResponse.Status.ACTIVE, responses.get(0).status());
        assertEquals(2, responses.size());
    }

    @Test
    void should_return_a_list_of_card_holders() {
        when(repository.findAll()).thenReturn(List.of(entityFactory(), entityFactory(), entityFactory(), entityFactory()));
        List<CardHolderResponse> responses = service.getCardHoldersBy(null);
        assertEquals(4, responses.size());
    }
    public CardHolderEntity entityFactory() {
        return CardHolderEntity.builder()
                .clientId(UUID.randomUUID())
                .creditAnalysisId(UUID.randomUUID())
                .status(CardHolderEntity.Status.ACTIVE)
                .creditLimit(BigDecimal.TEN)
                .bankAccount(bankAccountEntityFactory())
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