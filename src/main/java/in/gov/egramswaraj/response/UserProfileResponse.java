package in.gov.egramswaraj.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileResponse {

	private Long id;

	private String name;

	private String email;

	private String designation;

	private String gender;

	private String mobile;

	private String organisationName;

	private Long userId;

	private LocalDateTime createdOn;

}