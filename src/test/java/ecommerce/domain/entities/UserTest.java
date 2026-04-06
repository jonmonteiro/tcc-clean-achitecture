package ecommerce.domain.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password123", Role.CUSTOMER);
        user.setUserId(UUID.randomUUID().toString());
    }

    @Test
    void testUserValidationSuccess() {
        assertDoesNotThrow(() -> user.validate());
    }

    @Test
    void testUserValidationNullEmail() {
        user.setEmail(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.validate());
        assertEquals("User email cannot be null or empty", exception.getMessage());
    }

    @Test
    void testUserValidationInvalidEmail() {
        user.setEmail("invalid-email");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.validate());
        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void testUserValidationNullPassword() {
        user.setPassword(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.validate());
        assertEquals("User password cannot be null or empty", exception.getMessage());
    }

    @Test
    void testUserValidationNullRole() {
        user.setRole(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> user.validate());
        assertEquals("User role cannot be null", exception.getMessage());
    }

    @Test
    void testValidateUserIdSuccess() {
        assertDoesNotThrow(() -> User.validateUserId(UUID.randomUUID().toString()));
    }

    @Test
    void testValidateUserIdNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> User.validateUserId(null));
        assertEquals("User ID cannot be null or empty", exception.getMessage());
    }
}
