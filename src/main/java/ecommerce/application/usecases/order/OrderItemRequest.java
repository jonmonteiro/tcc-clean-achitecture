package ecommerce.application.usecases.order;

public record OrderItemRequest(Long productId, Integer quantity) {}
