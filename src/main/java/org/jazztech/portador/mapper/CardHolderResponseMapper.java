package org.jazztech.portador.mapper;

import java.util.List;
import org.jazztech.portador.controller.response.CardHolderResponse;
import org.jazztech.portador.repository.entity.CardHolderEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CardHolderResponseMapper {
    CardHolderResponse from(CardHolderEntity cardHolderEntity);

    List<CardHolderResponse> from(List<CardHolderEntity> cardHolderEntities);
}
