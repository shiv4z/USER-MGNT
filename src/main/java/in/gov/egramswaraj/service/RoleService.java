package in.gov.egramswaraj.service;

import org.springframework.data.domain.Page;

import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.request.RoleRequest;

public interface RoleService {

	RoleEntity createRole(RoleRequest request);

	String updateRoleStatus(Long roleId, boolean flag);

	String deleteRole(Long id);

	Page<RoleEntity> getAllRoles(int page, int size);

	RoleEntity getRoleById(Long id);

	RoleEntity updateRole(Long id, RoleRequest request);

	Page<PrivilegeEntity> getPrivilegesByRoleId(Long roleId, int page, int size);

}
