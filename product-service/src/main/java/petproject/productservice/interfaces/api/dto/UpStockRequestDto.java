package petproject.productservice.interfaces.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpStockRequestDto(
        @NotNull
        @Positive
        Integer amount
) {
}
