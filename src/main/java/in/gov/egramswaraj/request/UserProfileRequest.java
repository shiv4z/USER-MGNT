package in.gov.egramswaraj.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must be less than 255 characters")
    private String name;

    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must be less than 255 characters")
    private String email;

    @NotBlank(message = "Designation must not be blank")
    @Size(max = 255, message = "Designation must be less than 255 characters")
    private String designation;

    @NotBlank(message = "Gender must not be blank")
    @Pattern(regexp = "^(?i)(male|female|other)$", message = "Gender must be 'male', 'female', or 'other'")
    private String gender;

    @NotBlank(message = "Mobile number must not be blank")
    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
    private String mobile;

    @NotBlank(message = "Organisation name must not be blank")
    @Size(max = 255, message = "Organisation name must be less than 255 characters")
    private String organisationName;
    
    @NotNull(message = "User id should not be null")
    private Long userId;

    @Schema(hidden = true)
    private Long createdBy;
    
    @Schema(hidden = true)
    private Long updatedBy;
}
