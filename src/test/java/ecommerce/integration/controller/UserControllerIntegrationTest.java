package ecommerce.integration.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import jakarta.servlet.ServletException;
import ecommerce.domain.entities.Role;
import ecommerce.domain.entities.User;
import ecommerce.infra.DTO.RegisterRequestDTO;
import ecommerce.integration.config.AbstractIntegrationTest;

@DisplayName("Integration Tests - User Controller")
class UserControllerIntegrationTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("Should register user successfully")
    void testRegisterUserSuccess() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole("CUSTOMER");

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.role").value("CUSTOMER"));

        User savedUser = userRepository.findByEmail("newuser@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("newuser@example.com");
        assertThat(savedUser.getRole()).isEqualTo(Role.CUSTOMER);
    }

    @Test
    @DisplayName("Should register manager successfully")
    void testRegisterManagerSuccess() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("manager@example.com");
        registerRequest.setPassword("managerpass");
        registerRequest.setRole("MANAGER");

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.email").value("manager@example.com"))
                .andExpect(jsonPath("$.role").value("MANAGER"));

        User savedUser = userRepository.findByEmail("manager@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getRole()).isEqualTo(Role.MANAGER);
    }

    @Test
    @DisplayName("Should return error with invalid role")
    void testRegisterUserWithInvalidRole() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("invalidrole@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setRole("INVALID_ROLE");

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)));
        });
        
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("Invalid role");
    }

    @Test
    @DisplayName("Should return error with missing email")
    void testRegisterUserWithMissingEmail() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setPassword("password123");

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)));
        });
        
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("email cannot be null or empty");
    }

    @Test
    @DisplayName("Should return error with missing password")
    void testRegisterUserWithMissingPassword() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("nopass@example.com");

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(post("/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registerRequest)));
        });
        
        assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
        assertThat(exception.getCause().getMessage()).contains("password cannot be null or empty");
    }

    @Test
    @DisplayName("Should encrypt password")
    void testPasswordEncryption() throws Exception {
        RegisterRequestDTO registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("encrypted@example.com");
        registerRequest.setPassword("plainpassword");

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        User savedUser = userRepository.findByEmail("encrypted@example.com").orElse(null);
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getPassword()).isNotEqualTo("plainpassword");
        assertThat(savedUser.getPassword()).startsWith("$2");
    }
}
