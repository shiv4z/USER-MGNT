package in.gov.egramswaraj.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationMappingRequest {
	@NotNull(message = "application id can't be null")
	private Long applicationId;

	@NotEmpty(message = "user ids list can't be empty")
	private List<Long> userIds;
	
	@NotNull(message = "group id can't be null")
	private Long groupId;
}
