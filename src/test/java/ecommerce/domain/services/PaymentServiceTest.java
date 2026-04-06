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
import ecommerce.application.gateways.payment.PaymentRepository;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.OrderItem;
import ecommerce.domain.entities.Payment;
import ecommerce.domain.entities.PaymentStatus;
import ecommerce.domain.entities.Product;
import ecommerce.domain.entities.User;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private PaymentService paymentService;

    private User user;
    private Order order;
    private OrderItem orderItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUserId(UUID.randomUUID().toString());

        order = new Order(user);
        order.setOrderId(1L);

        Product product = new Product();
        product.setPrice(BigDecimal.valueOf(100.0));

        orderItem = new OrderItem(BigDecimal.valueOf(100.0), 1, product, order);
    }

    @Test
    void testProcessPaymentSuccessfully() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of(orderItem));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.processPayment(1L, user, "valid-key");

        assertNotNull(payment);
        assertEquals(PaymentStatus.CONFIRMED, payment.getStatus());
        verify(paymentRepository, times(2)).save(any(Payment.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testProcessPaymentAlreadyProcessed() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.of(new Payment()));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processPayment(1L, user, "valid-key");
        });
        assertEquals("Payment already processed for this order", exception.getMessage());
    }

    @Test
    void testProcessPaymentOrderNotFound() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processPayment(1L, user, "valid-key");
        });
        assertEquals("Order not found", exception.getMessage());
    }

    @Test
    void testProcessPaymentOrderDoesNotBelongToUser() {
        User anotherUser = new User();
        anotherUser.setUserId(UUID.randomUUID().toString());
        order.setUser(anotherUser);

        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processPayment(1L, user, "valid-key");
        });
        assertEquals("Order does not belong to this user", exception.getMessage());
    }

    @Test
    void testProcessPaymentOrderHasNoItems() {
        when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.processPayment(1L, user, "valid-key");
        });
        assertEquals("Order has no items", exception.getMessage());
    }
}
