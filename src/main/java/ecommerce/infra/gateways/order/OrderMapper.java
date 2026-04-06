package ecommerce.infra.gateways.order;

import ecommerce.domain.entities.Order;
import ecommerce.domain.entities.OrderStatus;
import ecommerce.infra.gateways.user.UserMapper;
import ecommerce.infra.persistense.order.OrderEntity;
import ecommerce.infra.persistense.user.UserEntity;
import ecommerce.infra.persistense.user.UserEntityRepository;

public class OrderMapper {

    private final UserMapper userMapper;
    private final UserEntityRepository userEntityRepository;

    public OrderMapper(UserMapper userMapper, UserEntityRepository userEntityRepository) {
        this.userMapper = userMapper;
        this.userEntityRepository = userEntityRepository;
    }

    public Order toDomain(OrderEntity entity) {
        Order order = new Order();
        order.setOrderId(entity.getId());
        order.setUser(userMapper.toDomain(entity.getUser()));
        order.setCreatedAt(entity.getCreatedAt());
        order.setStatus(entity.getStatus());
        return order;
    }

    public OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        if (order.getOrderId() != null) {
            entity.setId(order.getOrderId());
        }

        UserEntity userEntity = userEntityRepository.findById(order.getUser().getUserId())
                .orElseThrow(() -> new RuntimeException("User not found: " + order.getUser().getUserId()));
        entity.setUser(userEntity);
        entity.setCreatedAt(order.getCreatedAt());
        entity.setStatus(order.getStatus() != null ? order.getStatus() : OrderStatus.PENDING);
        return entity;
    }
}
