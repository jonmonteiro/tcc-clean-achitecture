package ecommerce.application.usecases.payment;

import ecommerce.application.gateways.user.UserRepository;
import ecommerce.domain.entities.Payment;
import ecommerce.domain.entities.User;
import ecommerce.domain.services.PaymentService;

public class ProcessPaymentAppService {

    private final PaymentService paymentService;
    private final UserRepository userRepository;

    public ProcessPaymentAppService(
            PaymentService paymentService,
            UserRepository userRepository) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
    }

    public Payment processPayment(Long orderId, String userId, String paymentKey) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        return paymentService.processPayment(orderId, user, paymentKey);
    }
}
