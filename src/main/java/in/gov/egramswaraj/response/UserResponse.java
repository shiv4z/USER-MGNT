package in.gov.egramswaraj.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String username;
	private boolean isActive;
	private String description;
	private LocalDateTime createdOn;
	
	
}
