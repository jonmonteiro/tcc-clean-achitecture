package ecommerce.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ecommerce.application.gateways.user.UserRepository;
import ecommerce.application.usecases.order.CreateOrderAppService;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.User;
import ecommerce.domain.services.OrderService;
import ecommerce.application.usecases.order.OrderItemRequest;

class CreateOrderAppServiceTest {

    @Mock
    private OrderService orderService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateOrderAppService createOrderAppService;

    private String userId;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID().toString();
        user = new User();
        user.setUserId(userId);
    }

    @Test
    void testCreateOrderSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(orderService.createOrder(any(User.class), anyList())).thenReturn(new Order(user));

        Order result = createOrderAppService.createOrder(userId, Collections.singletonList(new OrderItemRequest(1L, 1)));

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(orderService).createOrder(any(User.class), anyList());
    }

    @Test
    void testCreateOrderUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            createOrderAppService.createOrder(userId, Collections.singletonList(new OrderItemRequest(1L, 1)));
        });

        assertEquals("User not found", exception.getMessage());
    }
}
