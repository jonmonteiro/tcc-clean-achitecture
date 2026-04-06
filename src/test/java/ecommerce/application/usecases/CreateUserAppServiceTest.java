package ecommerce.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ecommerce.application.gateways.user.UserRepository;
import ecommerce.application.usecases.user.CreateUserAppService;
import ecommerce.domain.entities.Role;
import ecommerce.domain.entities.User;

class CreateUserAppServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateUserAppService createUserAppService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testInsertWithEmail() {
        User user = new User("test@example.com", "default_password", Role.CUSTOMER);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.createUser(any(User.class))).thenReturn(user);

        User result = createUserAppService.insert("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).createUser(any(User.class));
    }

    @Test
    void testInsertWithEmailPasswordAndRole() {
        User user = new User("admin@example.com", "adminpass", Role.MANAGER);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(userRepository.createUser(any(User.class))).thenReturn(user);

        User result = createUserAppService.insert("admin@example.com", "adminpass", Role.MANAGER);

        assertNotNull(result);
        assertEquals("admin@example.com", result.getEmail());
        assertEquals(Role.MANAGER, result.getRole());
        verify(userRepository).createUser(any(User.class));
    }

    @Test
    void testInsertWithExistingEmail() {
        User existing = new User("test@example.com", "pass", Role.CUSTOMER);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existing));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            createUserAppService.insert("test@example.com");
        });
        assertEquals("Email already registered: test@example.com", exception.getMessage());
        verify(userRepository, never()).createUser(any());
    }
}
