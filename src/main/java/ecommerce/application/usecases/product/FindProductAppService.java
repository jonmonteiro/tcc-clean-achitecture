package ecommerce.application.usecases.product;

import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.domain.entities.Product;

public class FindProductAppService {

    private final ProductRepository productRepository;

    public FindProductAppService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
    }
}
