package in.gov.egramswaraj.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleMappingRequest {
	private Long userId;

	private Long roleId;

	private List<Long> ids;
}
