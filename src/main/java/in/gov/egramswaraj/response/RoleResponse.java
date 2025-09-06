package in.gov.egramswaraj.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponse {
	private Long id;
	private String roleName;
	private String roleDescription;
	private boolean isActive;
	private LocalDateTime createdOn;
}
