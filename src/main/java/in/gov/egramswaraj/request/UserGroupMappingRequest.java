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
public class UserGroupMappingRequest {
	
    @NotNull(message = "Group ID must not be null")
    private Long groupId;

    @NotEmpty(message = "User IDs list must not be empty")
    private List<Long> userIds;

}
