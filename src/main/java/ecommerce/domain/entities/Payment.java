package ecommerce.domain.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
  
    private Long paymentId;

    private User user;

    private BigDecimal amount;
    
    private LocalDateTime createdAt;

    private PaymentStatus status;
    
    private Order order;

    private static final BigDecimal MINIMUM_PAYMENT_AMOUNT = new BigDecimal("1.00");

    public Payment() {
    }

    public Payment(User user, BigDecimal amount, Order order) {
        validatePaymentCreation(user, amount);
        this.user = user;
        this.amount = amount;
        this.order = order;
        this.createdAt = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }

    public Payment(User user, BigDecimal amount, Long paymentId, PaymentStatus status, Order order) {
        this.user = user;
        this.amount = amount;
        this.paymentId = paymentId;
        this.status = status != null ? status : PaymentStatus.PENDING;
        this.order = order;
    }

    private static void validatePaymentCreation(User user, BigDecimal amount) {
        if (user == null) {
            throw new IllegalArgumentException("Payment must have a user");
        }
        if (user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("Payment user must have a valid ID");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Payment amount cannot be null");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Payment amount must be greater than zero");
        }
        if (amount.compareTo(MINIMUM_PAYMENT_AMOUNT) < 0) {
            throw new IllegalArgumentException(
                String.format("Payment amount must be at least R$ %.2f", MINIMUM_PAYMENT_AMOUNT)
            );
        }
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long id) {
        this.paymentId = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
    
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void markAsError() {
        this.status = PaymentStatus.ERROR;
    }

    public void confirm() {
        this.status = PaymentStatus.CONFIRMED;
    }
}
