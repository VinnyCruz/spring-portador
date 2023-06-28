package org.jazztech.portador.mapper;

import org.jazztech.portador.controller.response.CreditCardResponse;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCardResponseMapper {
    CreditCardResponse from(CreditCardEntity creditCardEntity);
}
