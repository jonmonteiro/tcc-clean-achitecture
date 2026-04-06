package ecommerce.domain.entities;

public class User {

    private String userId;
    private String email;
    private String password;
    private Role role;

    public User() {
    }

    public User(String email){
        this.email = email;
        this.role = Role.CUSTOMER; // Default role
    }

    public User(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role != null ? role : Role.CUSTOMER;
        validate();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // Regras de negócio
    public void validate() {
        if (this.email == null || this.email.trim().isEmpty()) {
            throw new IllegalArgumentException("User email cannot be null or empty");
        }
        if (!this.email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (this.password == null || this.password.trim().isEmpty()) {
            throw new IllegalArgumentException("User password cannot be null or empty");
        }
        if (this.role == null) {
            throw new IllegalArgumentException("User role cannot be null");
        }
    }

    public static void validateUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
    }
}
