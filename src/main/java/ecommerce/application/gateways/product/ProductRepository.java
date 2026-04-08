package ecommerce.application.gateways.product;

import java.util.List;
import java.util.Optional;

import ecommerce.domain.entities.Product;

public interface ProductRepository  {
    Product createProduct(Product product);
    Optional<Product> findById(Long productId);
    Product updateProduct(Product product);
    List<Product> findAll();
    void deleteProduct(Long productId);
}
