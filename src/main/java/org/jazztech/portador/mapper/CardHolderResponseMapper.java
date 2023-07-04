package org.jazztech.portador.mapper;

import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderResponseMapper {
    CardHolderResponse from(CardHolderEntity cardHolderEntity);
}
