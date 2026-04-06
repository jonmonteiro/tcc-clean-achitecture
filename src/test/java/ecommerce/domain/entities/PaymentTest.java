package ecommerce.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PaymentTest {

    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password", Role.CUSTOMER);
        user.setUserId(UUID.randomUUID().toString());
        order = new Order(user);
    }

    @Test
    void testPaymentCreationSuccess() {
        Payment payment = new Payment(user, new BigDecimal("100.00"), order);
        assertNotNull(payment);
        assertEquals(user, payment.getUser());
        assertEquals(0, new BigDecimal("100.00").compareTo(payment.getAmount()));
        assertEquals(order, payment.getOrder());
        assertEquals(PaymentStatus.PENDING, payment.getStatus());
    }

    @Test
    void testPaymentCreationNullUser() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Payment(null, new BigDecimal("100.00"), order));
        assertEquals("Payment must have a user", exception.getMessage());
    }

    @Test
    void testPaymentCreationNullAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Payment(user, null, order));
        assertEquals("Payment amount cannot be null", exception.getMessage());
    }

    @Test
    void testPaymentCreationZeroAmount() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new Payment(user, BigDecimal.ZERO, order));
        assertEquals("Payment amount must be greater than zero", exception.getMessage());
    }
    
    @Test
    void testConfirmPayment() {
        Payment payment = new Payment(user, new BigDecimal("100.00"), order);
        payment.confirm();
        assertEquals(PaymentStatus.CONFIRMED, payment.getStatus());
    }

    @Test
    void testMarkAsError() {
        Payment payment = new Payment(user, new BigDecimal("100.00"), order);
        payment.markAsError();
        assertEquals(PaymentStatus.ERROR, payment.getStatus());
    }
}
