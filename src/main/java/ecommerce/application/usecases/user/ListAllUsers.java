package ecommerce.application.usecases.user;

import java.util.List;

import ecommerce.application.gateways.user.UserRepository;
import ecommerce.domain.entities.User;

public class ListAllUsers {
    private final UserRepository repository;

    public ListAllUsers(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> find() {
        return repository.listAllUsers();
    }
}
