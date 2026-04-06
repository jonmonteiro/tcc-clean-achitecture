package ecommerce.infra.DTO;

import java.math.BigDecimal;

public record StockResponseDTO(
    Long productId,
    String name,
    BigDecimal price,
    Integer availableStock,
    Boolean inStock
) {}
