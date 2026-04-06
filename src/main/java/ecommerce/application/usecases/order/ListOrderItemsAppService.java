package ecommerce.application.usecases.order;

import java.util.List;

import ecommerce.application.gateways.order.OrderItemRepository;
import ecommerce.domain.entities.OrderItem;

public class ListOrderItemsAppService {

    private final OrderItemRepository orderItemRepository;

    public ListOrderItemsAppService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
