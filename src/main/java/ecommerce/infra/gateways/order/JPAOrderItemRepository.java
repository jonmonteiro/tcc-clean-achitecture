package ecommerce.infra.gateways.order;

import java.util.List;
import java.util.stream.Collectors;

import ecommerce.application.gateways.order.OrderItemRepository;
import ecommerce.domain.entities.OrderItem;
import ecommerce.infra.persistense.order.OrderItemEntity;
import ecommerce.infra.persistense.order.OrderItemEntityRepository;
import jakarta.transaction.Transactional;

public class JPAOrderItemRepository implements OrderItemRepository {

    private final OrderItemEntityRepository orderItemEntityRepository;
    private final OrderItemMapper mapper;

    public JPAOrderItemRepository(OrderItemEntityRepository orderItemEntityRepository,
                                   OrderItemMapper mapper) {
        this.orderItemEntityRepository = orderItemEntityRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderItem save(OrderItem item) {
        OrderItemEntity entity = mapper.toEntity(item);
        OrderItemEntity savedEntity = orderItemEntityRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemEntityRepository.findByOrderId(orderId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void removeItem(Long itemId, String userId) {
        orderItemEntityRepository.deleteByIdAndOrderUserUserId(itemId, userId);
    }
}
