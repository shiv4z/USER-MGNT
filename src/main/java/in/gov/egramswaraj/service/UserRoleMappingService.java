package in.gov.egramswaraj.service;

import java.util.List;

public interface UserRoleMappingService {

	void assignRolesToUser(Long userId, List<Long> ids);

	void assignUsersToRole(List<Long> ids, Long roleId);

}
