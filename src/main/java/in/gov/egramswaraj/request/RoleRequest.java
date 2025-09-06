package in.gov.egramswaraj.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequest {

	@NotBlank(message = "Role name should not be blank")
	@Size(min = 4, max = 50, message = "Password must be between 4 and 50 characters")
	@Pattern(regexp = "^[A-Za-z]+$", message = "Role name must contain only alphabets")
	private String roleName;

	@NotBlank(message = "Role description should not be blank")
	@Size(min = 10, max = 255, message = "Password must be between 10 and 255 characters")
	private String roleDescription;

	@Schema(hidden = true)
	private Long createdBy;
	
	@Schema(hidden = true)
	private Long updatedBy;

}
