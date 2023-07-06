package org.jazztech.portador.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LimitUpdateResponseMapper {
    LimitUpdateResponse from(LimitUpdateRequest limitUpdateRequest);
}
