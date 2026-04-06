package ecommerce.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.math.BigDecimal;
import java.util.Arrays;
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
import ecommerce.domain.entities.Product;
import ecommerce.domain.entities.Role;
import ecommerce.domain.entities.User;
import ecommerce.infra.DTO.OrderItemRequestDTO;
import ecommerce.infra.DTO.OrderRequestDTO;
import ecommerce.integration.config.AbstractIntegrationTest;

@DisplayName("Integration Tests - Order Controller")
class OrderControllerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private User testUser;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setupTestData() {
        testUser = new User();
        testUser.setEmail("ordertest@example.com");
        testUser.setPassword(passwordEncoder.encode("password"));
        testUser.setRole(Role.CUSTOMER);
        testUser = userRepository.createUser(testUser);

        product1 = new Product();
        product1.setName("Product 1");
        product1.setPrice(BigDecimal.valueOf(10.00));
        product1.setQuantity(100);
        product1 = productRepository.createProduct(product1);

        product2 = new Product();
        product2.setName("Product 2");
        product2.setPrice(BigDecimal.valueOf(20.00));
        product2.setQuantity(50);
        product2 = productRepository.createProduct(product2);
    }

    @Test
    @DisplayName("Should create order successfully")
    @WithMockUser(username = "ordertest@example.com", roles = "CUSTOMER")
    void testCreateOrderSuccess() throws Exception {
        OrderItemRequestDTO item1 = new OrderItemRequestDTO(product1.getId(), 2);
        OrderItemRequestDTO item2 = new OrderItemRequestDTO(product2.getId(), 1);
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList(item1, item2));

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").exists())
                .andExpect(jsonPath("$.userId").value(testUser.getUserId()))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.totalAmount").value(40.00));

        List<Order> orders = orderRepository.findOrdersByUserId(testUser.getUserId());
        assertThat(orders).hasSize(1);
        List<OrderItem> items = orderItemRepository.findByOrderId(orders.get(0).getOrderId());
        assertThat(items).hasSize(2);
    }

    @Test
    @DisplayName("Should create order with single item")
    @WithMockUser(username = "ordertest@example.com", roles = "CUSTOMER")
    void testCreateOrderWithSingleItem() throws Exception {
        OrderItemRequestDTO item = new OrderItemRequestDTO(product1.getId(), 5);
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList(item));

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(1))
                .andExpect(jsonPath("$.totalAmount").value(50.00));
    }

    @Test
    @DisplayName("Should calculate total correctly")
    @WithMockUser(username = "ordertest@example.com", roles = "CUSTOMER")
    void testOrderTotalCalculation() throws Exception {
        OrderItemRequestDTO item1 = new OrderItemRequestDTO(product1.getId(), 3);
        OrderItemRequestDTO item2 = new OrderItemRequestDTO(product2.getId(), 2);
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList(item1, item2));

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(70.00)); // (3*10) + (2*20)
    }

    @Test
    @DisplayName("Should reduce product stock")
    @WithMockUser(username = "ordertest@example.com", roles = "CUSTOMER")
    void testOrderReducesStock() throws Exception {
        int initialQuantity = product1.getQuantity();
        OrderItemRequestDTO item = new OrderItemRequestDTO(product1.getId(), 5);
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList(item));

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        Product updatedProduct = productRepository.findById(product1.getId()).orElseThrow();
        assertThat(updatedProduct.getQuantity()).isEqualTo(initialQuantity - 5);
    }

    @Test
    @DisplayName("Should return 403 without authentication")
    void testCreateOrderWithoutAuth() throws Exception {
        OrderItemRequestDTO item = new OrderItemRequestDTO(product1.getId(), 1);
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList(item));

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should throw exception with empty items list")
    @WithMockUser(username = "ordertest@example.com", roles = "CUSTOMER")
    void testCreateOrderWithEmptyItems() throws Exception {
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList());

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/orders/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(orderRequest)));
        });

        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("at least one item");
    }

    @Test
    @DisplayName("Should group items under single Order")
    @WithMockUser(username = "ordertest@example.com", roles = "CUSTOMER")
    void testOrderItemsGroupedUnderOneOrder() throws Exception {
        OrderItemRequestDTO item1 = new OrderItemRequestDTO(product1.getId(), 1);
        OrderItemRequestDTO item2 = new OrderItemRequestDTO(product2.getId(), 1);
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList(item1, item2));

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk());

        List<Order> orders = orderRepository.findOrdersByUserId(testUser.getUserId());
        assertThat(orders).hasSize(1);

        List<OrderItem> items = orderItemRepository.findByOrderId(orders.get(0).getOrderId());
        assertThat(items).hasSize(2);
        assertThat(items.get(0).getOrder().getOrderId()).isEqualTo(items.get(1).getOrder().getOrderId());
    }

    @Test
    @DisplayName("Should create order with large quantity")
    @WithMockUser(username = "ordertest@example.com", roles = "CUSTOMER")
    void testCreateOrderWithLargeQuantity() throws Exception {
        OrderItemRequestDTO item = new OrderItemRequestDTO(product1.getId(), 50);
        OrderRequestDTO orderRequest = new OrderRequestDTO(Arrays.asList(item));

        mockMvc.perform(post("/orders/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(500.00));
    }
}
