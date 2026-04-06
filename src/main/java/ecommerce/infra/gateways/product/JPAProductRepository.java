package ecommerce.infra.gateways.product;

import java.util.Optional;
import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.domain.entities.Product;
import ecommerce.infra.persistense.product.ProductEntity;
import ecommerce.infra.persistense.product.ProductEntityRepository;

public class JPAProductRepository implements ProductRepository {

    private final ProductEntityRepository repository;
    private final ProductMapper mapper;

    public JPAProductRepository(ProductEntityRepository productRepository, ProductMapper productMapper) {
        this.repository = productRepository;
        this.mapper = productMapper;
    }

    @Override
    public Product createProduct(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return repository.findById(productId)
                .map(mapper::toDomain);
    }

    @Override
    public Product updateProduct(Product product) {
        ProductEntity entity = mapper.toEntity(product);
        ProductEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public java.util.List<Product> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }

}
