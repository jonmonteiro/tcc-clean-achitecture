package ecommerce.infra.persistense.order;

import ecommerce.infra.persistense.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {

    List<OrderEntity> findByUserUserId(String userId);

    Optional<OrderEntity> findByIdAndUserUserId(Long id, String userId);

    @Modifying
    @Transactional
    void deleteByIdAndUserUserId(Long id, String userId);

    @Modifying
    @Transactional
    void deleteAllByUser(UserEntity user);
}
