package ecommerce.application.usecases.order;

import ecommerce.application.gateways.order.OrderItemRepository;

public class RemoveOrderItem {

    private final OrderItemRepository orderItemRepository;

    public RemoveOrderItem(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void removeOrder(Long itemId, String userId) {
        orderItemRepository.removeItem(itemId, userId);
    }
}
