package ecommerce.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.application.usecases.product.CreateProductAppService;
import ecommerce.domain.entities.Product;

class CreateProductAppServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CreateProductAppService createProductAppService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsert() {
        Product product = new Product("New Product", new BigDecimal("25.50"), 100);
        when(productRepository.createProduct(any(Product.class))).thenReturn(product);

        Product result = createProductAppService.insert("New Product", new BigDecimal("25.50"), 100);

        assertNotNull(result);
        assertEquals("New Product", result.getName());
        verify(productRepository).createProduct(any(Product.class));
    }
}
