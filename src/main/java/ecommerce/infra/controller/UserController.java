package ecommerce.infra.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ecommerce.application.usecases.user.CreateUserAppService;
import ecommerce.application.usecases.user.DeleteUserAppService;
import ecommerce.domain.entities.Role;
import ecommerce.domain.entities.User;
import ecommerce.infra.DTO.RegisterRequestDTO;
import ecommerce.infra.DTO.RegisterResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUserAppService createUser;
    private final DeleteUserAppService deleteUser;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(CreateUserAppService createUser, DeleteUserAppService deleteUser) {
        this.createUser = createUser;
        this.deleteUser = deleteUser;
    }

    @PostMapping("/register")
    public RegisterResponseDTO registerUser(@RequestBody @Valid RegisterRequestDTO data) {
        Role role = Role.CUSTOMER;
        if (data.getRole() != null && !data.getRole().isEmpty()) {
            try {
                role = Role.valueOf(data.getRole().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid role. Use CUSTOMER or MANAGER");
            }
        }
        
        String encodedPassword = passwordEncoder.encode(data.getPassword());
        User saved = createUser.insert(data.getEmail(), encodedPassword, role);
        
        return new RegisterResponseDTO(saved.getUserId(), saved.getEmail(), saved.getRole().name());
    }

    /*@GetMapping("/list")
    public List<UserResponseDTO> listAllUsers() {
        return listAllUsers.find().stream()
                .map(user -> new UserResponseDTO(user.getUserId(), user.getEmail()))
                .collect(Collectors.toList());
    }*/

    /*@PutMapping("/update/{userId}") 
    public UserResponseDTO updateUser(@PathVariable String userId, @RequestBody @Valid UserRequestDTO data) {
        User updated = updateUser.update(userId, data.email());
        
        return new UserResponseDTO(updated.getUserId(), updated.getEmail());
    }*/

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable String userId) {
        deleteUser.remove(userId);
    }

}