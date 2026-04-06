package ecommerce.infra.gateways.user;

import java.util.List;
import java.util.stream.Collectors;
import ecommerce.application.gateways.user.UserRepository;
import ecommerce.domain.entities.User;
import ecommerce.infra.persistense.user.UserEntity;
import ecommerce.infra.persistense.user.UserEntityRepository;

public class JPAUserRepositoy implements UserRepository {

    private final UserEntityRepository repository;
    private final UserMapper mapper;

    public JPAUserRepositoy(UserEntityRepository repository, UserMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public User createUser(User user) {
        UserEntity entity = mapper.toEntity(user);
        UserEntity savedEntity = repository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<User> listAllUsers() {
        return repository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(String userId) {
        repository.deleteById(userId);
    }

    @Override
    public User updateUser(String userId, User data) {
        UserEntity entity = mapper.toEntity(data);
        entity.setUserId(userId);
        UserEntity updatedEntity = repository.save(entity);
        return mapper.toDomain(updatedEntity);
    }

    @Override
    public java.util.Optional<User> findById(String userId) {
        return repository.findById(userId)
                .map(mapper::toDomain);
    }

    @Override
    public java.util.Optional<User> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(mapper::toDomain);
    }
}
