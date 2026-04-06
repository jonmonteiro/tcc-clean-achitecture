package ecommerce.domain.entities;

import java.math.BigDecimal;

public class OrderItem {

    private Long id;
    private BigDecimal price;
    private Integer quantity;
    private Product product;
    private Order order;

    public OrderItem(BigDecimal price, Integer quantity, Product product, Order order) {
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("OrderItem price must be greater than zero");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("OrderItem quantity must be greater than zero");
        }
        if (product == null) {
            throw new IllegalArgumentException("OrderItem must have a product");
        }
        if (order == null) {
            throw new IllegalArgumentException("OrderItem must belong to an order");
        }
        this.price = price;
        this.quantity = quantity;
        this.product = product;
        this.order = order;
    }

    public OrderItem() {}

    public BigDecimal getTotal() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
