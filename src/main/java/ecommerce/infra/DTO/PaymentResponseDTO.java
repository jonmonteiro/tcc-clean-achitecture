package ecommerce.infra.DTO;

import ecommerce.domain.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    private Long paymentId;
    private String userEmail;
    private Double amount;
    private LocalDateTime createdAt;
    private PaymentStatus status;
    private Long orderId;
}
