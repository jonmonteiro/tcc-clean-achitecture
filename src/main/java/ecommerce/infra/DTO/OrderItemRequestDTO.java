package ecommerce.infra.DTO;

public record OrderItemRequestDTO(
    Long productId,
    Integer quantity
) {
    
}
