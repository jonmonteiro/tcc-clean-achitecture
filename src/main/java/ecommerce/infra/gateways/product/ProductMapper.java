package ecommerce.infra.gateways.product;

import ecommerce.domain.entities.Product;
import ecommerce.infra.persistense.product.ProductEntity;

public class ProductMapper {

    public ProductEntity toEntity(Product product) {
        ProductEntity entity = new ProductEntity(
                product.getName(),
                product.getPrice(),
                product.getQuantity()
        );
        entity.setId(product.getId());
        return entity;
    }

    public Product toDomain(ProductEntity entity) {
        Product product = new Product(
                entity.getName(),
                entity.getPrice(),
                entity.getQuantity()
        );
        product.setId(entity.getId());
        return product;
    }
}
