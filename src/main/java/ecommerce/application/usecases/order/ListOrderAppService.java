package ecommerce.application.usecases.order;

import java.util.List;

import ecommerce.application.gateways.order.OrderRepository;
import ecommerce.domain.entities.Order;

public class ListOrderAppService {

    private final OrderRepository orderRepository;

    public ListOrderAppService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> findOrders(String userId) {
        return orderRepository.findOrdersByUserId(userId);
    }
}
