package ecommerce.infra.gateways.payment;

import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.Payment;
import ecommerce.infra.gateways.order.OrderMapper;
import ecommerce.infra.gateways.user.UserMapper;
import ecommerce.infra.persistense.order.OrderEntity;
import ecommerce.infra.persistense.order.OrderEntityRepository;
import ecommerce.infra.persistense.payment.PaymentEntity;
import ecommerce.infra.persistense.user.UserEntity;
import ecommerce.infra.persistense.user.UserEntityRepository;
import jakarta.persistence.EntityNotFoundException;

public class PaymentMapper {

    private final UserMapper userMapper;
    private final UserEntityRepository userRepository;
    private final OrderMapper orderMapper;
    private final OrderEntityRepository orderEntityRepository;

    public PaymentMapper(UserMapper userMapper, UserEntityRepository userRepository,
                         OrderMapper orderMapper, OrderEntityRepository orderEntityRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.orderMapper = orderMapper;
        this.orderEntityRepository = orderEntityRepository;
    }

    public PaymentEntity toEntity(Payment payment) {
        UserEntity userEntity = userRepository.findById(payment.getUser().getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + payment.getUser().getUserId()));

        OrderEntity orderEntity = orderEntityRepository.findById(payment.getOrder().getOrderId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + payment.getOrder().getOrderId()));

        PaymentEntity entity = new PaymentEntity(userEntity, payment.getAmount(), orderEntity);

        if (payment.getPaymentId() != null) {
            entity.setPaymentId(payment.getPaymentId());
            entity.setCreatedAt(payment.getCreatedAt());
            entity.setStatus(payment.getStatus());
        }

        return entity;
    }

    public Payment toDomain(PaymentEntity entity) {
        Order order = entity.getOrder() != null ? orderMapper.toDomain(entity.getOrder()) : null;
        Payment payment = new Payment(
                userMapper.toDomain(entity.getUser()),
                entity.getAmount(),
                entity.getPaymentId(),
                entity.getStatus(),
                order
        );
        payment.setCreatedAt(entity.getCreatedAt());
        return payment;
    }
}
