package ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.application.usecases.product.CreateProductAppService;
import ecommerce.application.usecases.product.FindProductAppService;
import ecommerce.application.usecases.product.ListAllProducts;
import ecommerce.application.usecases.product.UpdateProductStockAppService;
import ecommerce.application.usecases.product.UpdateProductAppService;
import ecommerce.application.usecases.product.DeleteProductAppService;
import ecommerce.infra.gateways.product.ProductMapper;
import ecommerce.infra.gateways.product.JPAProductRepository;
import ecommerce.infra.persistense.product.ProductEntityRepository;

@Configuration
public class ProductConfig {

	@Bean
	CreateProductAppService createProduct(ProductRepository productRepository) {
		return new CreateProductAppService(productRepository);
	}

	@Bean
	FindProductAppService findProduct(ProductRepository productRepository) {
		return new FindProductAppService(productRepository);
	}

	@Bean
	UpdateProductStockAppService updateProductStock(ProductRepository productRepository) {
		return new UpdateProductStockAppService(productRepository);
	}

	@Bean
	ListAllProducts listAllProducts(ProductRepository productRepository) {
		return new ListAllProducts(productRepository);
	}

	@Bean
	UpdateProductAppService updateProduct(ProductRepository productRepository) {
		return new UpdateProductAppService(productRepository);
	}

	@Bean
	DeleteProductAppService deleteProduct(ProductRepository productRepository) {
		return new DeleteProductAppService(productRepository);
	}

	@Bean
	JPAProductRepository createProductService(ProductEntityRepository repository, ProductMapper mapper) {
		return new JPAProductRepository(repository, mapper);
	}

	@Bean
	ProductMapper productMapper() {
		return new ProductMapper();
	}

}
