package ecommerce.application.usecases.user;

import ecommerce.application.gateways.user.UserRepository;
import ecommerce.domain.entities.User;

public class UpdateUserAppService {

    private final UserRepository userRepository;

    public UpdateUserAppService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User update(String userId, String email) {
        User.validateUserId(userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        existingUser.setEmail(email);
        return userRepository.updateUser(userId, existingUser);
    }
}
