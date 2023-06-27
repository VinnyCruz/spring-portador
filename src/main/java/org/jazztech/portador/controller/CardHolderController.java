package org.jazztech.portador.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jazztech.portador.controller.request.CardHolderRequest;
import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.service.create.CreateCardHolderService;
import org.jazztech.portador.service.search.SearchCardHolderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "v1.0/card-holders")
@RequiredArgsConstructor
public class CardHolderController {
    private final CreateCardHolderService createCardHolderService;
    private final SearchCardHolderService searchCardHolderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardHolderResponse registerCardHolder(@RequestBody CardHolderRequest cardHolderRequest) {
        return createCardHolderService.createCardHolder(cardHolderRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CardHolderResponse> getCardHoldersBy(@RequestParam(value = "status", required = false) String status) {
        return searchCardHolderService.getCardHoldersBy(status);
    }
}
