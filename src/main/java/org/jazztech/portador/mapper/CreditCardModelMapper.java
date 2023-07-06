package org.jazztech.portador.mapper;

import java.util.List;
import org.jazztech.portador.controller.request.CreditCardRequest;
import org.jazztech.portador.model.CreditCardModel;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCardModelMapper {
    CreditCardModel from(CreditCardRequest creditCardRequest);

    List<CreditCardModel> listFrom(List<CreditCardEntity> creditCardEntityList);
}
