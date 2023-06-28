package org.jazztech.portador.mapper;

import org.jazztech.portador.model.CreditCardModel;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCardEntityMapper {
    CreditCardEntity from(CreditCardModel creditCardModel);
}
