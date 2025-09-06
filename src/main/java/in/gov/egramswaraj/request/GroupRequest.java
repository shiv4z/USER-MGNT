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
public class GroupRequest {
	
    @NotBlank(message = "Group name must not be blank")
    @Size(min = 3, max = 50, message = "Group name must be between 3 and 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Group name must contain only alphabets")
    private String groupName;

    @NotBlank(message = "Group description must not be blank")
    @Size(max = 255, message = "Description must be under 255 characters")
    private String groupDescription;
    
	@Schema(hidden = true)
	private Long createdBy;
	
	@Schema(hidden = true)
	private Long updatedBy;
}
