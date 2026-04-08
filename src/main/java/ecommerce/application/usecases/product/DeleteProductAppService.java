package ecommerce.application.usecases.product;

import ecommerce.application.gateways.product.ProductRepository;

public class DeleteProductAppService {

    private final ProductRepository productRepository;

    public DeleteProductAppService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void execute(Long productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + productId));
        
        productRepository.deleteProduct(productId);
    }
}
