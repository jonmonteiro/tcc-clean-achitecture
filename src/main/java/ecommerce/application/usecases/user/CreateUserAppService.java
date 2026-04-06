package ecommerce.application.usecases.user;

import ecommerce.application.gateways.user.UserRepository;
import ecommerce.domain.entities.Role;
import ecommerce.domain.entities.User;

public class CreateUserAppService {

    private final UserRepository userRepository;

    public CreateUserAppService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User insert(String email) {
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new IllegalArgumentException("Email already registered: " + email);
        });
        User user = new User(email);
        return userRepository.createUser(user);
    }

    public User insert(String email, String password, Role role) {
        userRepository.findByEmail(email).ifPresent(existing -> {
            throw new IllegalArgumentException("Email already registered: " + email);
        });
        User user = new User(email, password, role);
        return userRepository.createUser(user);
    }
}
