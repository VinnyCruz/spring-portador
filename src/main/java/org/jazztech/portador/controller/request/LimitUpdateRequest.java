package org.jazztech.portador.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import lombok.Builder;

@Builder(toBuilder = true)
public record LimitUpdateRequest(
        @Positive(message = "The limit needs to be positive.")
        @NotNull(message = "Please, specify the new limit.")
        BigDecimal newLimit
) {}
