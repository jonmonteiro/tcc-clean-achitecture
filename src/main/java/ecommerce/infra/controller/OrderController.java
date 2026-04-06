package ecommerce.infra.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ecommerce.application.usecases.order.CreateOrderAppService;
import ecommerce.application.usecases.order.ListOrderAppService;
import ecommerce.application.usecases.order.ListOrderItemsAppService;
import ecommerce.application.usecases.order.OrderItemRequest;
import ecommerce.application.usecases.order.RemoveOrderItem;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.OrderItem;
import ecommerce.infra.DTO.OrderRequestDTO;
import ecommerce.infra.DTO.OrderItemResponseDTO;
import ecommerce.infra.DTO.OrderResponseDTO;
import ecommerce.infra.security.AuthenticationHelper;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final CreateOrderAppService createOrder;
    private final ListOrderAppService listOrder;
    private final ListOrderItemsAppService listOrderItems;
    private final RemoveOrderItem removeOrderItem;

    @Autowired
    private AuthenticationHelper authHelper;

    public OrderController(CreateOrderAppService createOrder,
                           ListOrderAppService listOrder,
                           ListOrderItemsAppService listOrderItems,
                           RemoveOrderItem removeOrderItem) {
        this.createOrder = createOrder;
        this.listOrder = listOrder;
        this.listOrderItems = listOrderItems;
        this.removeOrderItem = removeOrderItem;
    }

    @PostMapping("/")
    public OrderResponseDTO createOrder(@RequestBody OrderRequestDTO orderRequest) {
        String userId = authHelper.getAuthenticatedUserId();

        List<OrderItemRequest> itemsData = orderRequest.items().stream()
                .map(dto -> new OrderItemRequest(dto.productId(), dto.quantity()))
                .collect(Collectors.toList());

        Order order = createOrder.createOrder(userId, itemsData);

        List<OrderItem> items = listOrderItems.findByOrderId(order.getOrderId());

        List<OrderItemResponseDTO> itemDTOs = items.stream()
                .map(item -> new OrderItemResponseDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getPrice().doubleValue(),
                        item.getQuantity(),
                        item.getTotal().doubleValue()
                ))
                .collect(Collectors.toList());

        Double totalAmount = items.stream()
                .mapToDouble(item -> item.getTotal().doubleValue())
                .sum();

        return new OrderResponseDTO(order.getOrderId(), order.getUser().getUserId(), order.getCreatedAt(),
                order.getStatus(), itemDTOs, totalAmount);
    }

    @GetMapping("/")
    public List<OrderResponseDTO> listUserOrders() {
        String userId = authHelper.getAuthenticatedUserId();
        List<Order> orders = listOrder.findOrders(userId);

        return orders.stream()
                .map(order -> {
                    List<OrderItem> items = listOrderItems.findByOrderId(order.getOrderId());

                    List<OrderItemResponseDTO> itemDTOs = items.stream()
                            .map(item -> new OrderItemResponseDTO(
                                    item.getId(),
                                    item.getProduct().getId(),
                                    item.getPrice().doubleValue(),
                                    item.getQuantity(),
                                    item.getTotal().doubleValue()
                            ))
                            .collect(Collectors.toList());

                    Double totalAmount = items.stream()
                            .mapToDouble(item -> item.getTotal().doubleValue())
                            .sum();

                    return new OrderResponseDTO(order.getOrderId(), order.getUser().getUserId(),
                            order.getCreatedAt(), order.getStatus(), itemDTOs, totalAmount);
                })
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{itemId}")
    public void removeOrderItem(@PathVariable Long itemId) {
        String userId = authHelper.getAuthenticatedUserId();
        removeOrderItem.removeOrder(itemId, userId);
    }
}
