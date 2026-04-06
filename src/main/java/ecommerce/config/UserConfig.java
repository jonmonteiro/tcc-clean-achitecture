package ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ecommerce.application.gateways.user.UserRepository;
import ecommerce.application.usecases.user.CreateUserAppService;
import ecommerce.application.usecases.user.DeleteUserAppService;
import ecommerce.application.usecases.user.ListAllUsers;
import ecommerce.application.usecases.user.UpdateUserAppService;
import ecommerce.infra.gateways.user.UserMapper;
import ecommerce.infra.gateways.user.JPAUserRepositoy;
import ecommerce.infra.persistense.user.UserEntityRepository;

@Configuration
public class UserConfig {

    @Bean
    CreateUserAppService createUser(UserRepository userRepository) {
        return new CreateUserAppService(userRepository);
    }

    @Bean
    ListAllUsers listAllUsers(UserRepository repository) {
        return new ListAllUsers(repository);
    }

    @Bean
    UpdateUserAppService updateUser(UserRepository userRepository) {
        return new UpdateUserAppService(userRepository);
    }

    @Bean
    DeleteUserAppService deleteUser(UserRepository repository) {
        return new DeleteUserAppService(repository);
    }

    @Bean
    JPAUserRepositoy creatUserService(UserEntityRepository repository, UserMapper mapper) {
        return new JPAUserRepositoy(repository, mapper);
    }

    @Bean
    UserMapper userMapper() {
        return new UserMapper();
    }

}
