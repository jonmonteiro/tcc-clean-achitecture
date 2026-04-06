package ecommerce.domain.entities;

import java.math.BigDecimal;

public class Product {

    private Long id;    
    private String name;
    private BigDecimal price;
    private Integer quantity;

    public Product(String name, BigDecimal price, Integer quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        validate();
    }

    public Product() {
        
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    // Regras de negócio
    public void validate() {
        if (this.name == null || this.name.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty");
        }
        if (this.price == null || this.price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Product price must be greater than zero");
        }
        if (this.quantity == null || this.quantity < 0) {
            throw new IllegalArgumentException("Product quantity cannot be negative");
        }
    }

    // Controle de estoque
    public boolean hasStock(int requestedQuantity) {
        return this.quantity >= requestedQuantity;
    }

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to decrease must be positive");
        }
        if (!hasStock(quantity)) {
            throw new IllegalStateException(
                String.format("Insufficient stock for product '%s'. Available: %d, Requested: %d", 
                this.name, this.quantity, quantity)
            );
        }
        this.quantity -= quantity;
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity to increase must be positive");
        }
        this.quantity += quantity;
    }
}
