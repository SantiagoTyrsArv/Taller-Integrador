package u.tallerin.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import u.tallerin.domain.enums.PaymentMethod;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private BigDecimal amount;
    private LocalDateTime date;
    private PaymentMethod paymentMethod;
    private BigDecimal amountPaid;
    private BigDecimal change;
    private String cardNumber;
    private String customerName;
    private String authorization;
}
