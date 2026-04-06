package ecommerce.infra.gateways.user;

import ecommerce.domain.entities.User;
import ecommerce.infra.persistense.user.UserEntity;

public class UserMapper {

    public UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity(user.getEmail(), user.getPassword(), user.getRole());
        if (user.getUserId() != null) {
            entity.setUserId(user.getUserId());
        }
        return entity;
    }

    public User toDomain(UserEntity entity) {
        User user = new User(entity.getEmail(), entity.getPassword(), entity.getRole());
        user.setUserId(entity.getUserId());
        return user;
    }
}
