package in.gov.egramswaraj.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrivilegeResponse {
	private Long id;
	private String privilegeName;
	private String privilegeUrl;
	private String description;
	private boolean isActive;
	private LocalDateTime createdOn;
}
