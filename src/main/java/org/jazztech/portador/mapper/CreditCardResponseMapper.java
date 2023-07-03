package org.jazztech.portador.mapper;

import java.util.List;
import org.jazztech.portador.controller.response.CreditCardResponse;
import org.jazztech.portador.model.CreditCardModel;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCardResponseMapper {
    CreditCardResponse from(CreditCardEntity creditCardEntity);

    List<CreditCardResponse> from(List<CreditCardModel> creditCardModel);

}
