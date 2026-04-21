package ecommerce.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import jakarta.servlet.ServletException;
import java.math.BigDecimal;
import ecommerce.domain.entities.Product;
import ecommerce.infra.DTO.ProductDTO;
import ecommerce.integration.config.AbstractIntegrationTest;

@DisplayName("Integration Tests - Product Controller")
class ProductControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Should create product successfully")
    @WithMockUser(roles = "MANAGER")
    void testCreateProductSuccess() throws Exception {
        ProductDTO productDTO = new ProductDTO("Laptop", new BigDecimal("1500.00"), 10);

        mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1500.00))
                .andExpect(jsonPath("$.quantity").value(10));

        assertThat(productRepository.findAll()).hasSize(1);
        Product savedProduct = productRepository.findAll().get(0);
        assertThat(savedProduct.getName()).isEqualTo("Laptop");
        assertThat(savedProduct.getPrice()).isEqualByComparingTo(new BigDecimal("1500.00"));
        assertThat(savedProduct.getQuantity()).isEqualTo(10);
    }

    @Test
    @DisplayName("Should create product with zero quantity")
    @WithMockUser(roles = "MANAGER")
    void testCreateProductWithZeroQuantity() throws Exception {
        ProductDTO productDTO = new ProductDTO("Out of Stock Item", new BigDecimal("99.99"), 0);

        mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Out of Stock Item"))
                .andExpect(jsonPath("$.price").value(99.99))
                .andExpect(jsonPath("$.quantity").value(0));
    }

    @Test
    @DisplayName("Should create product with high quantity")
    @WithMockUser(roles = "MANAGER")
    void testCreateProductWithHighQuantity() throws Exception {
        ProductDTO productDTO = new ProductDTO("Bulk Item", new BigDecimal("5.50"), 10000);

        mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity").value(10000));
    }

    @Test
    @DisplayName("Should handle decimal prices")
    @WithMockUser(roles = "MANAGER")
    void testCreateProductWithDecimalPrice() throws Exception {
        ProductDTO productDTO = new ProductDTO("Small Item", new BigDecimal("0.99"), 100);

        mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.price").value(0.99));
    }

    @Test
    @DisplayName("Should throw exception with null name")
    @WithMockUser(roles = "MANAGER")
    void testCreateProductWithNullName() throws Exception {
        ProductDTO productDTO = new ProductDTO(null, new BigDecimal("100.00"), 5);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/products/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDTO)));
        });
        
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("name cannot be null or empty");
    }

    @Test
    @DisplayName("Should throw exception with null price")
    @WithMockUser(roles = "MANAGER")
    void testCreateProductWithNullPrice() throws Exception {
        ProductDTO productDTO = new ProductDTO("Product", null, 5);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/products/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDTO)));
        });
        
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("Product price must be greater than zero");
    }

    /*@Test
    @DisplayName("Should throw exception with null quantity")
    @WithMockUser(roles = "MANAGER")
    void testCreateProductWithNullQuantity() throws Exception {
        ProductDTO productDTO = new ProductDTO("Product", new BigDecimal("100.00"), null);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/products/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productDTO)));
        });
        
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("Product quantity cannot be negative");
    }*/

    @Test
    @DisplayName("Should return 403 without authentication")
    void testCreateProductWithoutAuth() throws Exception {
        ProductDTO productDTO = new ProductDTO("Unauthorized Product", new BigDecimal("100.00"), 5);

        mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should create multiple products")
    @WithMockUser(roles = "MANAGER")
    void testCreateMultipleProducts() throws Exception {
        ProductDTO product1 = new ProductDTO("Product 1", new BigDecimal("10.00"), 5);
        ProductDTO product2 = new ProductDTO("Product 2", new BigDecimal("20.00"), 10);

        mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product1)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product2)))
                .andExpect(status().isOk());

        assertThat(productRepository.findAll()).hasSize(2);
    }
}
