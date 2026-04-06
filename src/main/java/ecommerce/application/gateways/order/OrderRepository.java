package ecommerce.application.gateways.order;

import java.util.List;
import java.util.Optional;

import ecommerce.domain.entities.Order;

public interface OrderRepository {
    Order save(Order order);
    List<Order> findOrdersByUserId(String userId);
    Optional<Order> findById(Long orderId);
    void removeOrder(Long orderId, String userId);
}
