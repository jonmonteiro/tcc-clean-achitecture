package ecommerce.infra.persistense.user;

import ecommerce.domain.entities.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;
    private String email;
    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role;

    public UserEntity(String email){
        this.email = email;
        this.role = Role.CUSTOMER;
    }
    
    public UserEntity(String email, String password, Role role){
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
