package u.tallerin.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    private String phone;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String address;
}
