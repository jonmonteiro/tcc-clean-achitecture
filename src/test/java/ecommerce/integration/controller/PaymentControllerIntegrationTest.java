package ecommerce.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import jakarta.servlet.ServletException;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.OrderItem;
import ecommerce.domain.entities.Payment;
import ecommerce.domain.entities.Product;
import ecommerce.domain.entities.Role;
import ecommerce.domain.entities.User;
import ecommerce.infra.DTO.PaymentRequestDTO;
import ecommerce.integration.config.AbstractIntegrationTest;

@DisplayName("Integration Tests - Payment Controller")
class PaymentControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Product testProduct;
    private Order testOrder;
    private Long orderId;

    @BeforeEach
    void setupTestData() {
        testUser = new User();
        testUser.setEmail("paymenttest@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole(Role.CUSTOMER);
        testUser = userRepository.createUser(testUser);

        testProduct = new Product();
        testProduct.setName("Test Product");
        testProduct.setPrice(BigDecimal.valueOf(100.00));
        testProduct.setQuantity(10);
        testProduct = productRepository.createProduct(testProduct);

        testOrder = new Order(testUser);
        testOrder = orderRepository.save(testOrder);
        orderId = testOrder.getOrderId();

        OrderItem item = new OrderItem(
                BigDecimal.valueOf(100.00),
                2,
                testProduct,
                testOrder
        );
        orderItemRepository.save(item);
    }

    @Test
    @DisplayName("Should process payment successfully")
    @WithMockUser(username = "paymenttest@example.com", roles = "CUSTOMER")
    void testProcessPaymentSuccess() throws Exception {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setPaymentKey("valid-payment-key-123");

        mockMvc.perform(post("/payments/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").exists())
                .andExpect(jsonPath("$.userEmail").value("paymenttest@example.com"))
                .andExpect(jsonPath("$.amount").value(200.00))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.createdAt").exists())
                .andExpect(jsonPath("$.status").exists());

        List<Payment> payments = paymentRepository.findByUserId(testUser.getUserId());
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getAmount()).isEqualByComparingTo(BigDecimal.valueOf(200.00));
    }

    @Test
    @DisplayName("Should create payment with correct amount from multiple items")
    @WithMockUser(username = "paymenttest@example.com", roles = "CUSTOMER")
    void testPaymentAmountCalculation() throws Exception {
        OrderItem secondItem = new OrderItem(
                BigDecimal.valueOf(100.00),
                1,
                testProduct,
                testOrder
        );
        orderItemRepository.save(secondItem);

        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setPaymentKey("payment-key");

        mockMvc.perform(post("/payments/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(300.00)); 
    }

    @Test
    @DisplayName("Should return 403 without authentication")
    void testProcessPaymentWithoutAuth() throws Exception {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setPaymentKey("payment-key");

        mockMvc.perform(post("/payments/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should handle payment with different keys")
    @WithMockUser(username = "paymenttest@example.com", roles = "CUSTOMER")
    void testPaymentWithDifferentKeys() throws Exception {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setPaymentKey("credit-card-key");

        mockMvc.perform(post("/payments/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").exists());

        assertThat(paymentRepository.findByUserId(testUser.getUserId())).hasSize(1);
    }

    @Test
    @DisplayName("Should throw exception when PIX key is null")
    @WithMockUser(username = "paymenttest@example.com", roles = "CUSTOMER")
    void testPaymentValidation() throws Exception {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setPaymentKey(null);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/payments/" + orderId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(paymentRequest)));
        });

        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("PIX key is required");
    }

    @Test
    @DisplayName("Should store payment metadata")
    @WithMockUser(username = "paymenttest@example.com", roles = "CUSTOMER")
    void testPaymentMetadata() throws Exception {
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();
        paymentRequest.setPaymentKey("metadata-test-key");

        mockMvc.perform(post("/payments/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest)))
                .andExpect(status().isOk());

        Payment savedPayment = paymentRepository.findByUserId(testUser.getUserId()).get(0);
        assertThat(savedPayment.getUser().getUserId()).isEqualTo(testUser.getUserId());
        assertThat(savedPayment.getCreatedAt()).isNotNull();
        assertThat(savedPayment.getOrder().getOrderId()).isEqualTo(orderId);
    }

    @Test
    @DisplayName("Should handle payment for different users' orders")
    @WithMockUser(username = "paymenttest@example.com", roles = "CUSTOMER")
    void testMultiplePayments() throws Exception {
        PaymentRequestDTO paymentRequest1 = new PaymentRequestDTO();
        paymentRequest1.setPaymentKey("payment-1");

        mockMvc.perform(post("/payments/" + orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paymentRequest1)))
                .andExpect(status().isOk());

        assertThat(paymentRepository.findByUserId(testUser.getUserId())).hasSize(1);
    }

}
