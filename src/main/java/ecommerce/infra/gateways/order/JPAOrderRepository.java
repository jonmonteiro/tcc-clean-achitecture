package ecommerce.infra.gateways.order;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ecommerce.application.gateways.order.OrderRepository;
import ecommerce.domain.entities.Order;
import ecommerce.infra.persistense.order.OrderEntity;
import ecommerce.infra.persistense.order.OrderEntityRepository;

public class JPAOrderRepository implements OrderRepository {

    private final OrderEntityRepository orderEntityRepository;
    private final OrderMapper mapper;

    public JPAOrderRepository(OrderEntityRepository repository, OrderMapper mapper) {
        this.orderEntityRepository = repository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        OrderEntity entity = mapper.toEntity(order);
        OrderEntity savedEntity = orderEntityRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<Order> findOrdersByUserId(String userId) {
        return orderEntityRepository.findByUserUserId(userId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderEntityRepository.findById(orderId)
                .map(mapper::toDomain);
    }

    @Override
    public void removeOrder(Long orderId, String userId) {
        orderEntityRepository.deleteByIdAndUserUserId(orderId, userId);
    }
}
