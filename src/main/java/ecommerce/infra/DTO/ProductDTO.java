package ecommerce.infra.DTO;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotEmpty;

public record ProductDTO(
    @NotEmpty
    String name,
    @NotEmpty
    BigDecimal price,
    @NotEmpty
    Integer quantity) {
   
}
