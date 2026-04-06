package ecommerce.application.usecases.product;

import java.math.BigDecimal;
import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.domain.entities.Product;

public class CreateProductAppService {

    private final ProductRepository productRepository;

    public CreateProductAppService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product insert(String name, BigDecimal price, int quantity) {
        Product product = new Product(name, price, quantity);
        return productRepository.createProduct(product);
    }
}
