package ecommerce.application.gateways.user;

import java.util.List;
import java.util.Optional;

import ecommerce.domain.entities.User;

public interface UserRepository {
    User createUser(User user);
    List<User> listAllUsers();
    void deleteUser(String userId);
    User updateUser(String userId, User user);
    Optional<User> findById(String userId);
    Optional<User> findByEmail(String email);
}
