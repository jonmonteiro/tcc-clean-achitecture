package ecommerce.application.usecases.user;

import ecommerce.application.gateways.user.UserRepository;

public class DeleteUserAppService {

    private final UserRepository repository;

    public DeleteUserAppService(UserRepository repository) {
        this.repository = repository;
    }

    public void remove(String userId) {
        repository.deleteUser(userId);
    }
}
