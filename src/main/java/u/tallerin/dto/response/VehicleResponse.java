package u.tallerin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import u.tallerin.domain.enums.VehicleType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleResponse {

    private Long id;
    private String plate;
    private String brand;
    private String model;
    private int year;
    private VehicleType type;
    private Long customerId;
}
