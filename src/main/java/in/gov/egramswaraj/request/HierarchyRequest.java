package in.gov.egramswaraj.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HierarchyRequest {
	
	@NotNull(message = "localbody code must not be null")
	@Min(value = 1, message = "localbody code must be a positive number")
	private Integer localbodyCode;

	@NotNull(message = "localbody version must not be null")
	@Min(value = 1, message = "version must be a positive number")
	private Integer version;
}
