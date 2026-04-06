package ecommerce.infra.DTO;

import ecommerce.domain.entities.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseDTO(
    Long orderId,
    String userId,
    LocalDateTime date,
    OrderStatus status,
    List<OrderItemResponseDTO> items,
    Double totalAmount
) {
    
}
