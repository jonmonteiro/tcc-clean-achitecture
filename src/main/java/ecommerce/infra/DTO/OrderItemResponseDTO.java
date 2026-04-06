package ecommerce.infra.DTO;

public record OrderItemResponseDTO(
    Long id,
    Long productId,
    Double price,
    Integer quantity,
    Double total
) {
    
}
