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
public class PrivilegeRequest {
	
	@NotBlank(message = "privilege name is required")
	@Size(min = 3, max = 100, message = "privilege name must be between 3 and 100 characters")
	@Pattern(regexp = "^[A-Za-z]+$", message = "privilege name must contain only alphabets")
	private String privilegeName;

	@NotBlank(message = "privilege URL is required")
	private String privilegeUrl;

	@Size(max = 255, message = "Description must not exceed 255 characters")
	private String description;
	
	@Schema(hidden = true)
	private Long createdBy;
	
	@Schema(hidden = true)
	private Long updatedBy;
}
