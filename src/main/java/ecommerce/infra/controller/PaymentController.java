package ecommerce.infra.controller;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ecommerce.application.usecases.payment.ListPaymentsAppService;
import ecommerce.application.usecases.payment.ProcessPaymentAppService;
import ecommerce.domain.entities.Payment;
import ecommerce.infra.DTO.PaymentRequestDTO;
import ecommerce.infra.DTO.PaymentResponseDTO;
import ecommerce.infra.security.AuthenticationHelper;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final ProcessPaymentAppService processPayment;
    private final ListPaymentsAppService listPayments;

    @Autowired
    private AuthenticationHelper authHelper;

    public PaymentController(ProcessPaymentAppService processPayment, ListPaymentsAppService listPayments) {
        this.processPayment = processPayment;
        this.listPayments = listPayments;
    }

    @PostMapping("/{orderId}")
    public PaymentResponseDTO processOrderPayment(@PathVariable Long orderId,
                                                  @RequestBody @Valid PaymentRequestDTO paymentRequest) {
        String userId = authHelper.getAuthenticatedUserId();

        Payment payment = processPayment.processPayment(
                orderId,
                userId,
                paymentRequest.getPaymentKey()
        );

        return new PaymentResponseDTO(
                payment.getPaymentId(),
                payment.getUser().getEmail(),
                payment.getAmount().doubleValue(),
                payment.getCreatedAt(),
                payment.getStatus(),
                payment.getOrder().getOrderId()
        );
    }

    @GetMapping("/")
    public List<PaymentResponseDTO> listUserPayments() {
        String userId = authHelper.getAuthenticatedUserId();
        List<Payment> payments = listPayments.findByUserId(userId);

        return payments.stream()
                .map(payment -> new PaymentResponseDTO(
                        payment.getPaymentId(),
                        payment.getUser().getEmail(),
                        payment.getAmount().doubleValue(),
                        payment.getCreatedAt(),
                        payment.getStatus(),
                        payment.getOrder().getOrderId()
                ))
                .collect(Collectors.toList());
    }
}
