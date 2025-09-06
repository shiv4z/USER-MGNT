package in.gov.egramswaraj.service;

import org.springframework.data.domain.Page;

import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.request.PrivilegeRequest;

public interface PrivilegeService {

	PrivilegeEntity createPrivilege(PrivilegeRequest request);

	String updatePrivilegeStatus(Long PrivilegeId, boolean flag);

	String deletePrivilege(Long id);

	Page<PrivilegeEntity> getAllPrivileges(int page, int size);

	PrivilegeEntity getPrivilegeById(Long id);

	PrivilegeEntity updatePrivilege(Long id, PrivilegeRequest request);

}
