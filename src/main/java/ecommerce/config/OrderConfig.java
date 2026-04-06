package ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ecommerce.application.gateways.order.OrderItemRepository;
import ecommerce.application.gateways.order.OrderRepository;
import ecommerce.application.gateways.product.ProductRepository;
import ecommerce.application.gateways.user.UserRepository;
import ecommerce.application.usecases.order.CreateOrderAppService;
import ecommerce.application.usecases.order.ListOrderAppService;
import ecommerce.application.usecases.order.ListOrderItemsAppService;
import ecommerce.application.usecases.order.RemoveOrderItem;
import ecommerce.domain.services.OrderService;
import ecommerce.infra.gateways.order.JPAOrderItemRepository;
import ecommerce.infra.gateways.order.JPAOrderRepository;
import ecommerce.infra.gateways.order.OrderItemMapper;
import ecommerce.infra.gateways.order.OrderMapper;
import ecommerce.infra.gateways.product.ProductMapper;
import ecommerce.infra.gateways.user.UserMapper;
import ecommerce.infra.persistense.order.OrderEntityRepository;
import ecommerce.infra.persistense.order.OrderItemEntityRepository;
import ecommerce.infra.persistense.product.ProductEntityRepository;
import ecommerce.infra.persistense.user.UserEntityRepository;

@Configuration
public class OrderConfig {

    @Bean
    CreateOrderAppService createOrder(OrderService orderService, UserRepository userRepository) {
        return new CreateOrderAppService(orderService, userRepository);
    }

    @Bean
    ListOrderAppService listOrder(OrderRepository orderRepository) {
        return new ListOrderAppService(orderRepository);
    }

    @Bean
    ListOrderItemsAppService listOrderItems(OrderItemRepository orderItemRepository) {
        return new ListOrderItemsAppService(orderItemRepository);
    }

    @Bean
    RemoveOrderItem removeOrderItem(OrderItemRepository orderItemRepository) {
        return new RemoveOrderItem(orderItemRepository);
    }

    @Bean
    OrderService orderService(OrderRepository orderRepository,
                              OrderItemRepository orderItemRepository,
                              ProductRepository productRepository) {
        return new OrderService(orderRepository, orderItemRepository, productRepository);
    }

    @Bean
    JPAOrderRepository jpaOrderRepository(OrderEntityRepository orderEntityRepository, OrderMapper orderMapper) {
        return new JPAOrderRepository(orderEntityRepository, orderMapper);
    }

    @Bean
    JPAOrderItemRepository jpaOrderItemRepository(OrderItemEntityRepository orderItemEntityRepository,
                                                   OrderItemMapper orderItemMapper) {
        return new JPAOrderItemRepository(orderItemEntityRepository, orderItemMapper);
    }

    @Bean
    OrderMapper orderMapper(UserMapper userMapper, UserEntityRepository userEntityRepository) {
        return new OrderMapper(userMapper, userEntityRepository);
    }

    @Bean
    OrderItemMapper orderItemMapper(ProductMapper productMapper,
                                    OrderMapper orderMapper,
                                    ProductEntityRepository productEntityRepository,
                                    OrderEntityRepository orderEntityRepository) {
        return new OrderItemMapper(productMapper, orderMapper, productEntityRepository, orderEntityRepository);
    }
}
