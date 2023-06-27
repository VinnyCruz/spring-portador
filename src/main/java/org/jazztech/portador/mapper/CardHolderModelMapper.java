package org.jazztech.portador.mapper;

import org.jazztech.portador.controller.request.CardHolderRequest;
import org.jazztech.portador.model.BankAccountModel;
import org.jazztech.portador.model.CardHolderModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderModelMapper {
    CardHolderModel from(CardHolderRequest cardHolderRequest);

    BankAccountModel from(CardHolderRequest.BankAccountRequest bankAccountRequest);
}
