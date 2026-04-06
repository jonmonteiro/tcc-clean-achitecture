package ecommerce.application.gateways.order;

import java.util.List;

import ecommerce.domain.entities.OrderItem;

public interface OrderItemRepository {
    OrderItem save(OrderItem item);
    List<OrderItem> findByOrderId(Long orderId);
    void removeItem(Long itemId, String userId);
}
