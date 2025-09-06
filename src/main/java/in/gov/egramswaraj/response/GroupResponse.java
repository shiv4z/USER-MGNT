package in.gov.egramswaraj.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupResponse {
	private Long id;
	private String groupName;
	private String groupDescription;
	private boolean isActive;
	private LocalDateTime createdOn;
}
