package org.jazztech.portador.mapper;

import org.jazztech.portador.model.BankAccountModel;
import org.jazztech.portador.model.CardHolderModel;
import org.jazztech.portador.repository.entity.BankAccountEntity;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderEntityMapper {
    CardHolderEntity from(CardHolderModel cardHolderModel);

    BankAccountEntity from(BankAccountModel bankAccountModel);
}
