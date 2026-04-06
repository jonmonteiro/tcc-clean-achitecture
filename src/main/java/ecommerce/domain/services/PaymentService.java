package ecommerce.domain.services;

import java.math.BigDecimal;
import java.util.List;
import ecommerce.application.gateways.order.OrderItemRepository;
import ecommerce.application.gateways.order.OrderRepository;
import ecommerce.application.gateways.payment.PaymentRepository;
import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.OrderItem;
import ecommerce.domain.entities.Payment;
import ecommerce.domain.entities.User;

public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository,
                          OrderItemRepository orderItemRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Payment processPayment(Long orderId, User user, String paymentKey) {
        if (user == null || user.getUserId() == null || user.getUserId().trim().isEmpty()) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (paymentKey == null || paymentKey.trim().isEmpty()) {
            throw new IllegalArgumentException("PIX key is required");
        }

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new IllegalArgumentException("Payment already processed for this order");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Order does not belong to this user");
        }

        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);

        if (orderItems == null || orderItems.isEmpty()) {
            throw new IllegalArgumentException("Order has no items");
        }

        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Payment payment = new Payment(user, totalAmount, order);

        Payment savedPayment = paymentRepository.save(payment);

        try {
            savedPayment.confirm();
            savedPayment = paymentRepository.save(savedPayment);

            // Confirmar o pedido após pagamento bem-sucedido
            order.confirm();
            orderRepository.save(order);
        } catch (Exception e) {
            handlePaymentError(savedPayment, e);

            // Cancelar o pedido se o pagamento falhar
            order.cancel();
            orderRepository.save(order);

            throw new IllegalArgumentException("Payment processing failed: " + e.getMessage());
        }

        return savedPayment;
    }

    private void handlePaymentError(Payment payment, Exception e) {
        try {
            payment.markAsError();
            paymentRepository.save(payment);
        } catch (Exception ex) {
            System.err.println("Failed to update payment status to ERROR: " + ex.getMessage());
        }
    }
}
