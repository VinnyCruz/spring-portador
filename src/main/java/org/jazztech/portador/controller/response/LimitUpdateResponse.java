package org.jazztech.portador.controller.response;

import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record LimitUpdateResponse(
        BigDecimal newLimit
) {}
