package ecommerce.infra.persistense.payment;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import ecommerce.domain.entities.PaymentStatus;
import ecommerce.infra.persistense.order.OrderEntity;
import ecommerce.infra.persistense.user.UserEntity;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private BigDecimal amount;
    
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    public PaymentEntity(UserEntity user, BigDecimal amount, OrderEntity order) {
        this.user = user;
        this.amount = amount;
        this.order = order;
        this.createdAt = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
    }
}