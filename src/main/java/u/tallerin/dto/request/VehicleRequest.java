package u.tallerin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import u.tallerin.domain.enums.VehicleType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleRequest {

    @NotBlank
    private String plate;

    @NotBlank
    private String brand;

    @NotBlank
    private String model;

    @Min(1886)
    private int year;

    @NotNull
    private VehicleType type;

    @NotNull
    private Long customerId;
}
