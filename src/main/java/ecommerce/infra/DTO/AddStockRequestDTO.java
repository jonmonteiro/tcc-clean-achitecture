package ecommerce.infra.DTO;

import jakarta.validation.constraints.Min;

public record AddStockRequestDTO(
    @Min(value = 1, message = "Quantity must be at least 1")
    int quantity
) {}
