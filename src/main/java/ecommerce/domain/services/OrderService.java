package ecommerce.domain.services;

import java.util.List;
import ecommerce.application.gateways.order.OrderItemRepository;
import ecommerce.application.gateways.order.OrderRepository;
import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.application.usecases.order.OrderItemRequest;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.OrderItem;
import ecommerce.domain.entities.Product;
import ecommerce.domain.entities.User;

public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productRepository = productRepository;
    }

    public Order createOrder(User user, List<OrderItemRequest> items) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        for (OrderItemRequest itemData : items) {
            Product product = productRepository.findById(itemData.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + itemData.productId()));

            if (!product.hasStock(itemData.quantity())) {
                throw new IllegalStateException(
                        String.format("Insufficient stock for product '%s'. Available: %d, Requested: %d",
                                product.getName(), product.getQuantity(), itemData.quantity()));
            }
        }

        Order order = new Order(user);
        Order savedOrder = orderRepository.save(order);

        for (OrderItemRequest itemData : items) {
            Product product = productRepository.findById(itemData.productId()).get();

            product.decreaseStock(itemData.quantity());
            productRepository.updateProduct(product);

            OrderItem orderItem = new OrderItem(
                    product.getPrice(),
                    itemData.quantity(),
                    product,
                    savedOrder);

            orderItemRepository.save(orderItem);
        }

        return savedOrder;
    }
}
