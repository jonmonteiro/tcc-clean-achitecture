package ecommerce.infra.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ecommerce.application.usecases.product.CreateProductAppService;
import ecommerce.application.usecases.product.FindProductAppService;
import ecommerce.application.usecases.product.ListAllProducts;
import ecommerce.application.usecases.product.UpdateProductStockAppService;
import ecommerce.domain.entities.Product;
import ecommerce.infra.DTO.AddStockRequestDTO;
import ecommerce.infra.DTO.ProductDTO;
import ecommerce.infra.DTO.StockResponseDTO;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final CreateProductAppService createProduct;
    private final FindProductAppService findProduct;
    private final UpdateProductStockAppService updateProductStock;
    private final ListAllProducts listAllProducts;

    public ProductController(
            CreateProductAppService createProduct,
            FindProductAppService findProduct,
            UpdateProductStockAppService updateProductStock,
            ListAllProducts listAllProducts) {
        this.createProduct = createProduct;
        this.findProduct = findProduct;
        this.updateProductStock = updateProductStock;
        this.listAllProducts = listAllProducts;
    }

    @PostMapping("/")
    public ProductDTO createProduct(@RequestBody @Valid ProductDTO data) {
        Product saved = createProduct.insert(data.name(), data.price(), data.quantity());
        
        return new ProductDTO(saved.getName(), saved.getPrice(), saved.getQuantity());
    }

    @GetMapping("/")
    public List<StockResponseDTO> listAllProducts() {
        List<Product> products = listAllProducts.execute();
        
        return products.stream()
                .map(product -> new StockResponseDTO(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getQuantity(),
                    product.getQuantity() > 0
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("/{productId}/stock")
    public StockResponseDTO checkStock(@PathVariable Long productId) {
        Product product = findProduct.execute(productId);
        return new StockResponseDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getQuantity(),
            product.getQuantity() > 0
        );
    }

    @PatchMapping("/{productId}/stock")
    public ProductDTO addStock(
            @PathVariable Long productId,
            @RequestBody @Valid AddStockRequestDTO data) {
        Product updated = updateProductStock.execute(productId, data.quantity());
        return new ProductDTO(updated.getName(), updated.getPrice(), updated.getQuantity());
    }
}
