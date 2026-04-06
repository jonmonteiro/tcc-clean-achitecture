package ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ecommerce.application.gateways.order.OrderItemRepository;
import ecommerce.application.gateways.order.OrderRepository;
import ecommerce.application.gateways.payment.PaymentRepository;
import ecommerce.application.gateways.user.UserRepository;
import ecommerce.application.usecases.payment.ListPaymentsAppService;
import ecommerce.application.usecases.payment.ProcessPaymentAppService;
import ecommerce.domain.services.PaymentService;
import ecommerce.infra.gateways.order.OrderMapper;
import ecommerce.infra.gateways.payment.JPAPaymentRepository;
import ecommerce.infra.gateways.payment.PaymentMapper;
import ecommerce.infra.gateways.user.UserMapper;
import ecommerce.infra.persistense.order.OrderEntityRepository;
import ecommerce.infra.persistense.payment.PaymentEntityRepository;
import ecommerce.infra.persistense.user.UserEntityRepository;

@Configuration
public class PaymentConfig {

    @Bean
    ProcessPaymentAppService processPayment(
            PaymentService paymentService,
            UserRepository userRepository) {
        return new ProcessPaymentAppService(paymentService, userRepository);
    }

    @Bean
    ListPaymentsAppService listPayments(
            PaymentRepository paymentRepository) {
        return new ListPaymentsAppService(paymentRepository);
    }

    @Bean
    PaymentService paymentService(PaymentRepository paymentRepository,
                                   OrderRepository orderRepository,
                                   OrderItemRepository orderItemRepository) {
        return new PaymentService(paymentRepository, orderRepository, orderItemRepository);
    }

    @Bean
    PaymentRepository paymentRepository(
            PaymentEntityRepository repository,
            PaymentMapper mapper) {
        return new JPAPaymentRepository(repository, mapper);
    }

    @Bean
    PaymentMapper paymentMapper(UserMapper userMapper, UserEntityRepository userRepository,
                                 OrderMapper orderMapper, OrderEntityRepository orderEntityRepository) {
        return new PaymentMapper(userMapper, userRepository, orderMapper, orderEntityRepository);
    }
}
