package ecommerce.infra.gateways.payment;

import ecommerce.application.gateways.payment.PaymentRepository;
import ecommerce.domain.entities.Payment;
import ecommerce.infra.persistense.payment.PaymentEntity;
import ecommerce.infra.persistense.payment.PaymentEntityRepository;
import jakarta.transaction.Transactional;

public class JPAPaymentRepository implements PaymentRepository {

    private final PaymentEntityRepository repository;
    private final PaymentMapper mapper;

    public JPAPaymentRepository(
            PaymentEntityRepository repository,
            PaymentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        PaymentEntity entity = mapper.toEntity(payment);
        PaymentEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }
    
    @Override
    public java.util.Optional<Payment> findByOrderId(Long orderId) {
        return repository.findByOrderId(orderId)
                .map(mapper::toDomain);
    }
    
    @Override
    public java.util.List<Payment> findByUserId(String userId) {
        return repository.findByUserUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(java.util.stream.Collectors.toList());
    }
}
