package in.gov.egramswaraj.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {

	@NotBlank(message = "New Password must not be blank")
	@Size(min = 8, max = 50, message = "password must be between 8 and 50 characters")
	@Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&^#_\\-+=(){}\\[\\]:;\"'<>,./~`|\\\\]).{8,50}$", message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
	private String newUserPazz;

	@NotBlank(message = "Old Password must not be blank")
	private String oldUserPazz;
}