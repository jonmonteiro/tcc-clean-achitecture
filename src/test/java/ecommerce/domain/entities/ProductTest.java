package ecommerce.domain.entities;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductTest {

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Laptop", new BigDecimal("1500.00"), 10);
    }

    @Test
    void testProductValidationSuccess() {
        assertDoesNotThrow(() -> product.validate());
    }

    @Test
    void testProductValidationNullName() {
        product.setName(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> product.validate());
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    @Test
    void testProductValidationInvalidPrice() {
        product.setPrice(BigDecimal.ZERO);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> product.validate());
        assertEquals("Product price must be greater than zero", exception.getMessage());
    }

    @Test
    void testProductValidationNegativeQuantity() {
        product.setQuantity(-1);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> product.validate());
        assertEquals("Product quantity cannot be negative", exception.getMessage());
    }

    @Test
    void testHasStock() {
        assertTrue(product.hasStock(5));
        assertTrue(product.hasStock(10));
        assertFalse(product.hasStock(11));
    }

    @Test
    void testDecreaseStockSuccess() {
        product.decreaseStock(5);
        assertEquals(5, product.getQuantity());
    }

    @Test
    void testDecreaseStockInsufficient() {
        Exception exception = assertThrows(IllegalStateException.class, () -> product.decreaseStock(11));
        assertTrue(exception.getMessage().contains("Insufficient stock"));
    }

    @Test
    void testDecreaseStockWithNegativeQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> product.decreaseStock(-1));
        assertEquals("Quantity to decrease must be positive", exception.getMessage());
    }

    @Test
    void testIncreaseStockSuccess() {
        product.increaseStock(5);
        assertEquals(15, product.getQuantity());
    }

    @Test
    void testIncreaseStockWithNegativeQuantity() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> product.increaseStock(-1));
        assertEquals("Quantity to increase must be positive", exception.getMessage());
    }
}
