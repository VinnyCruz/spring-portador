package org.jazztech.portador.mapper;

import org.jazztech.portador.controller.request.LimitUpdateRequest;
import org.jazztech.portador.controller.response.LimitUpdateResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LimitUpdateResponseMapper {
    LimitUpdateResponse from(LimitUpdateRequest limitUpdateRequest);
}
