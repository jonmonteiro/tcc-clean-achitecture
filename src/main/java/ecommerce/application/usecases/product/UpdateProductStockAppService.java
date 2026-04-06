package ecommerce.application.usecases.product;

import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.domain.entities.Product;

public class UpdateProductStockAppService {

    private final ProductRepository productRepository;

    public UpdateProductStockAppService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product execute(Long productId, int quantityToAdd) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        product.increaseStock(quantityToAdd);
        return productRepository.updateProduct(product);
    }
}
