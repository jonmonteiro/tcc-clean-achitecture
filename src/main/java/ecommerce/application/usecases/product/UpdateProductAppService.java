package ecommerce.application.usecases.product;

import java.math.BigDecimal;
import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.domain.entities.Product;

public class UpdateProductAppService {

    private final ProductRepository productRepository;

    public UpdateProductAppService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId, String name, BigDecimal price) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        
        if (name != null && !name.trim().isEmpty()) {
            product.setName(name);
        }
        
        if (price != null && price.compareTo(BigDecimal.ZERO) > 0) {
            product.setPrice(price);
        }
        
        product.validate();
        return productRepository.updateProduct(product);
    }
}
