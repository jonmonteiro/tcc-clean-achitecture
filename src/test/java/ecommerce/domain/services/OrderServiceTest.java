package ecommerce.domain.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ecommerce.application.gateways.order.OrderItemRepository;
import ecommerce.application.gateways.order.OrderRepository;
import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.application.usecases.order.OrderItemRequest;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.Product;
import ecommerce.domain.entities.User;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;
    private OrderItemRequest orderItemRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(UUID.randomUUID().toString());

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100.0));
        product.setQuantity(10);

        orderItemRequest = new OrderItemRequest(1L, 2);
    }

    @Test
    void testCreateOrderSuccessfully() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setOrderId(1L);
            return order;
        });

        Order createdOrder = orderService.createOrder(user, List.of(orderItemRequest));

        assertNotNull(createdOrder);
        assertEquals(1L, createdOrder.getOrderId());
        assertEquals(8, product.getQuantity());
        verify(productRepository, times(2)).findById(1L);
        verify(orderRepository, times(1)).save(any(Order.class));
        verify(productRepository, times(1)).updateProduct(product);
        verify(orderItemRepository, times(1)).save(any());
    }

    @Test
    void testCreateOrderWithNullUser() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(null, List.of(orderItemRequest));
        });
        assertEquals("User cannot be null", exception.getMessage());
    }

    @Test
    void testCreateOrderWithEmptyItems() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(user, Collections.emptyList());
        });
        assertEquals("Order must have at least one item", exception.getMessage());
    }

    @Test
    void testCreateOrderProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            orderService.createOrder(user, List.of(orderItemRequest));
        });
        assertEquals("Product not found with id: 1", exception.getMessage());
    }

    @Test
    void testCreateOrderInsufficientStock() {
        product.setQuantity(1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Exception exception = assertThrows(IllegalStateException.class, () -> {
            orderService.createOrder(user, List.of(orderItemRequest));
        });
        assertEquals("Insufficient stock for product 'Test Product'. Available: 1, Requested: 2", exception.getMessage());
    }
}
