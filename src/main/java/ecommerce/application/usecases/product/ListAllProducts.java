package ecommerce.application.usecases.product;

import java.util.List;
import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.domain.entities.Product;

public class ListAllProducts {

    private final ProductRepository productRepository;

    public ListAllProducts(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> execute() {
        return productRepository.findAll();
    }
}
