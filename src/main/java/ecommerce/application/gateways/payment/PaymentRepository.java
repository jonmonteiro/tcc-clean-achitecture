package ecommerce.application.gateways.payment;

import ecommerce.domain.entities.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findByOrderId(Long orderId);
    List<Payment> findByUserId(String userId);
}
