package in.gov.egramswaraj.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeRoleMappingRequest {
	@NotNull(message = "Role ID cannot be null")
	private Long roleId;

	@NotEmpty(message = "At least one resource ID must be provided")
	private List<Long> resourceIds;
}
