package org.jazztech.portador.mapper;

import java.util.List;
import org.jazztech.portador.model.CreditCardModel;
import org.jazztech.portador.repository.entity.CreditCardEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CreditCardEntityMapper {
    CreditCardEntity from(CreditCardModel creditCardModel);

    List<CreditCardEntity> from(List<CreditCardModel> creditCardModel);
}
