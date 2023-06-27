package org.jazztech.portador.service.search;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
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
import org.springframework.web.server.ResponseStatusException;

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
        when(repository.findByStatus("ACTIVE")).thenReturn(List.of(entityFactory(),entityFactory()));
        List<CardHolderResponse> responses = service.getCardHoldersBy("ACTIVE");
        assertEquals("ACTIVE", responses.get(0).status());
        assertEquals(2, responses.size());
    }

    @Test
    void should_return_a_list_of_card_holders() {
        when(repository.findAll()).thenReturn(List.of(entityFactory(), entityFactory(), entityFactory(), entityFactory()));
        List<CardHolderResponse> responses = service.getCardHoldersBy(null);
        assertEquals(4, responses.size());
    }

    @Test
    void should_return_a_card_holder_searched_by_id() {
        final UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.ofNullable(entityFactory()));

        CardHolderResponse response = service.getCardHolderById(uuid);
        assertNotNull(response);
    }

    @Test
    void should_throw_response_status_exception_not_found_when_search_a_card_holder_by_id() {
        final UUID uuid = UUID.randomUUID();
        when(repository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.getCardHolderById(uuid));
    }

    public CardHolderEntity entityFactory() {
        return CardHolderEntity.builder()
                .clientId(UUID.randomUUID())
                .creditAnalysisId(UUID.randomUUID())
                .status("ACTIVE")
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