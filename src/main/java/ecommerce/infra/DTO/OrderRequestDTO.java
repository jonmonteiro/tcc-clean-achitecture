package ecommerce.infra.DTO;

import java.util.List;

public record OrderRequestDTO(
    List<OrderItemRequestDTO> items
) {
    
}
