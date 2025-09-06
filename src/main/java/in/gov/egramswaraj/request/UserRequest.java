package in.gov.egramswaraj.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

	@NotBlank(message = "Username must not be blank")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	@Pattern(
		    regexp = "^[a-zA-Z0-9._@-]+$",
		    message = "Username can only contain letters, numbers, dots (.), hyphens (-), underscores (_), and at symbol (@)"
		)
	private String username;

	@NotBlank(message = "Password must not be blank")
	@Size(min = 8, max = 50, message = "password must be between 8 and 50 characters")
	@Pattern(
		    regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&^#_\\-+=(){}\\[\\]:;\"'<>,./~`|\\\\]).{8,50}$",
		    message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character"
		)
	private String password;
	
	@NotBlank(message = "user description must not be blank")
	private String description;
	
	
	@Schema(hidden = true)
	private Long createdBy;
	
	@Schema(hidden = true)
	private Long updatedBy;


	@NotNull(message = "localbody code must not be null")
	private Integer localbodyCode;

	@NotNull(message = "localbody version must not be null")
	private Integer version;

}
