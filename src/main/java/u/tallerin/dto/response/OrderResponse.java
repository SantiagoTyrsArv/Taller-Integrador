package u.tallerin.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import u.tallerin.domain.enums.OrderStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private LocalDateTime date;
    private OrderStatus status;
    private Long customerId;
    private Long vehicleId;
    private List<Long> serviceIds;
    private Map<Long, BigDecimal> appliedPrices;
    private BigDecimal total;
    private Long paymentId;
    private boolean fullyPaid;
}
