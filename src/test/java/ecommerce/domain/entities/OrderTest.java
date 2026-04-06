package ecommerce.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password", Role.CUSTOMER);
        user.setUserId(UUID.randomUUID().toString());
    }

    @Test
    void testOrderCreationSuccess() {
        Order order = new Order(user);
        assertNotNull(order);
        assertEquals(user, order.getUser());
        assertEquals(OrderStatus.PENDING, order.getStatus());
        assertNotNull(order.getCreatedAt());
    }

    @Test
    void testOrderCreationNullUser() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Order(null));
        assertEquals("Order must have a user", exception.getMessage());
    }

    @Test
    void testConfirmOrder() {
        Order order = new Order(user);
        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void testCancelOrder() {
        Order order = new Order(user);
        order.cancel();
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }
}
