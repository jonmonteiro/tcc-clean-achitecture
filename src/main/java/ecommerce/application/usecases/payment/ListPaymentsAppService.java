package ecommerce.application.usecases.payment;

import ecommerce.application.gateways.payment.PaymentRepository;
import ecommerce.domain.entities.Payment;
import java.util.List;

public class ListPaymentsAppService {

    private final PaymentRepository paymentRepository;

    public ListPaymentsAppService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findByUserId(String userId) {
        return paymentRepository.findByUserId(userId);
    }
}
