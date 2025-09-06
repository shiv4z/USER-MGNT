package in.gov.egramswaraj.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDeatilsResponse extends UserResponse {
	
	private List<UserHierarchyResponse> hierarchies;
	
	private List<ApplicationResponse> applications;

}
