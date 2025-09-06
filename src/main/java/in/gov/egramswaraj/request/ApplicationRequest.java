package in.gov.egramswaraj.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ApplicationRequest {
	
	@NotBlank(message = "Application name must not be blank")
    @Pattern(regexp = "^[A-Za-z ]+$", message = "Application name must contain only alphabets")
	private String applicationName;
	
	@NotBlank(message = "Application url must not be blank")
    private String applicationUrl;
	
	
	@Schema(hidden = true)
	private Long createdBy;
	
	@Schema(hidden = true)
	private Long updatedBy;

}
