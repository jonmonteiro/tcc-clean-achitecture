package ecommerce.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ecommerce.application.gateways.user.UserRepository;
import ecommerce.application.usecases.payment.ProcessPaymentAppService;
import ecommerce.domain.entities.Payment;
import ecommerce.domain.entities.User;
import ecommerce.domain.services.PaymentService;

class ProcessPaymentAppServiceTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProcessPaymentAppService processPaymentAppService;

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
    void testProcessPaymentSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(paymentService.processPayment(anyLong(), any(User.class), anyString())).thenReturn(new Payment());

        Payment result = processPaymentAppService.processPayment(1L, userId, "payment-key");

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(paymentService).processPayment(anyLong(), any(User.class), anyString());
    }

    @Test
    void testProcessPaymentUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            processPaymentAppService.processPayment(1L, userId, "payment-key");
        });

        assertEquals("User not found", exception.getMessage());
    }
}
