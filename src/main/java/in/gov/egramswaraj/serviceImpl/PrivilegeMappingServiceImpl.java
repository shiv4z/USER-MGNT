package in.gov.egramswaraj.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.entity.PrivilegeMappingEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.repo.PrivilegeMappingRepository;
import in.gov.egramswaraj.repo.PrivilegeRepository;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.service.PrivilegeMappingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrivilegeMappingServiceImpl implements PrivilegeMappingService {

	private final RoleRepository roleRepository;
	private final PrivilegeRepository privilegeRepository;
	private final PrivilegeMappingRepository PrivilegeMappingRepository;

	public String mapPrivilegesToRole(Long roleId, List<Long> privilegeIds) {
		RoleEntity role = roleRepository.findById(roleId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.ROLE_NOT_FOUND + roleId));

		List<PrivilegeEntity> resources = privilegeRepository.findAllById(privilegeIds);

		if (resources.size() != privilegeIds.size()) {
			throw new EntityNotFoundException(AppConstant.INVALID_PRIVILEGE);
		}

		List<PrivilegeMappingEntity> mappings = resources.stream().map(resource -> {
			PrivilegeMappingEntity mapping = new PrivilegeMappingEntity();
			mapping.setRole(role);
			mapping.setPrivilege(resource);

//		            ResourceRoleId id = new ResourceRoleId();
//		            id.setRoleId(role.getId());
//		            id.setResourceId(resource.getId());
//		            mapping.setId(id);

			return mapping;
		}).toList();

		PrivilegeMappingRepository.saveAll(mappings);

		return AppConstant.PRIVILEGE_ROLE_MAPPING;
	}

}
