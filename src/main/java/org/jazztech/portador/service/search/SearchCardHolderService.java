package org.jazztech.portador.service.search;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jazztech.portador.apicredit.CreditApi;
import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.mapper.CardHolderEntityMapper;
import org.jazztech.portador.mapper.CardHolderModelMapper;
import org.jazztech.portador.mapper.CardHolderResponseMapper;
import org.jazztech.portador.repository.CardHolderRepository;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchCardHolderService {
    private final CardHolderResponseMapper cardHolderResponseMapper;
    private final CardHolderModelMapper cardHolderModelMapper;
    private final CardHolderEntityMapper cardHolderEntityMapper;
    private final CardHolderRepository repository;
    private final CreditApi creditApi;

    public List<CardHolderResponse> getCardHoldersBy(String status) {
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

}
