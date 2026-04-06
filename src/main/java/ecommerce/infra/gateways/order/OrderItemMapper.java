package ecommerce.infra.gateways.order;

import ecommerce.domain.entities.OrderItem;
import ecommerce.infra.gateways.product.ProductMapper;
import ecommerce.infra.persistense.order.OrderEntity;
import ecommerce.infra.persistense.order.OrderEntityRepository;
import ecommerce.infra.persistense.order.OrderItemEntity;
import ecommerce.infra.persistense.product.ProductEntity;
import ecommerce.infra.persistense.product.ProductEntityRepository;

public class OrderItemMapper {

    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final ProductEntityRepository productEntityRepository;
    private final OrderEntityRepository orderEntityRepository;

    public OrderItemMapper(ProductMapper productMapper,
                           OrderMapper orderMapper,
                           ProductEntityRepository productEntityRepository,
                           OrderEntityRepository orderEntityRepository) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
        this.productEntityRepository = productEntityRepository;
        this.orderEntityRepository = orderEntityRepository;
    }

    public OrderItem toDomain(OrderItemEntity entity) {
        OrderItem item = new OrderItem();
        item.setId(entity.getId());
        item.setPrice(entity.getPrice());
        item.setQuantity(entity.getQuantity());
        item.setProduct(productMapper.toDomain(entity.getProduct()));
        item.setOrder(orderMapper.toDomain(entity.getOrder()));
        return item;
    }

    public OrderItemEntity toEntity(OrderItem item) {
        OrderItemEntity entity = new OrderItemEntity();
        if (item.getId() != null) {
            entity.setId(item.getId());
        }
        entity.setPrice(item.getPrice());
        entity.setQuantity(item.getQuantity());

        ProductEntity productEntity = productEntityRepository.findById(item.getProduct().getId())
                .orElseThrow(() -> new RuntimeException("Product not found: " + item.getProduct().getId()));
        entity.setProduct(productEntity);

        OrderEntity orderEntity = orderEntityRepository.findById(item.getOrder().getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + item.getOrder().getOrderId()));
        entity.setOrder(orderEntity);

        return entity;
    }
}
