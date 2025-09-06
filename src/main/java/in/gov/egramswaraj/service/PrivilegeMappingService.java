package in.gov.egramswaraj.service;

import java.util.List;

public interface PrivilegeMappingService {

	String mapPrivilegesToRole(Long roleId, List<Long> resourceIds);

}
