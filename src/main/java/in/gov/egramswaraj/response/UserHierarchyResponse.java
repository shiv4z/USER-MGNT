package in.gov.egramswaraj.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserHierarchyResponse {
	private Integer localbodyCode;
	private Integer version;
}
