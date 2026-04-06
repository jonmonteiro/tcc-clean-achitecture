package ecommerce.application.usecases.order;

import java.util.List;
import ecommerce.application.gateways.user.UserRepository;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.User;
import ecommerce.domain.services.OrderService;

public class CreateOrderAppService {

    private final OrderService orderService;
    private final UserRepository userRepository;

    public CreateOrderAppService(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    public Order createOrder(String userId, List<OrderItemRequest> items) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return orderService.createOrder(user, items);
    }
}
