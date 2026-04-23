package u.tallerin.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private Long id;
    private String name;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private List<Long> vehicleIds;
    private List<Long> orderIds;
}
