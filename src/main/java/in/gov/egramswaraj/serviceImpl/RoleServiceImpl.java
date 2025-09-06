package in.gov.egramswaraj.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.repo.PrivilegeMappingRepository;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.request.RoleRequest;
import in.gov.egramswaraj.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	private final PrivilegeMappingRepository PrivilegeMappingRepository;
	private final ModelMapper modelMapper;
	private final UserContext userContext;

	@Override
	public RoleEntity createRole(RoleRequest request) {
		if (roleRepository.existsByRoleName(request.getRoleName())) {
			throw new IllegalArgumentException(AppConstant.ROLE_ALREADY_EXIST);
		}
        request.setCreatedBy(userContext.getUserId());
		RoleEntity role = modelMapper.map(request, RoleEntity.class);
		return roleRepository.save(role);
	}

	@Override
	public String updateRoleStatus(Long roleId, boolean flag) {
		RoleEntity role = roleRepository.findById(roleId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.ROLE_NOT_FOUND + roleId));

		if (role.isActive() == flag) {
			return "Role is already " + (flag ? "active" : "inactive");
		}

		role.setActive(flag);
		role.setUpdatedBy(userContext.getUserId());
		roleRepository.save(role);
		return AppConstant.ROLE_STATUS;
	}

	@Override
	public String deleteRole(Long roleId) {
		RoleEntity role = roleRepository.findById(roleId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.ROLE_NOT_FOUND + roleId));

		roleRepository.delete(role);
		return AppConstant.ROLE_DELETION;
	}
	
	
	@Override
	public Page<RoleEntity> getAllRoles(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
		return roleRepository.findAll(pageable);
	}

	@Override
	public RoleEntity getRoleById(Long id) {
		return roleRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.ROLE_NOT_FOUND + id));
	}

	@Override
	public RoleEntity updateRole(Long roleId, RoleRequest request) {
		RoleEntity role = roleRepository.findById(roleId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.ROLE_NOT_FOUND + roleId));
        
		modelMapper.map(request, role);
		role.setUpdatedBy(userContext.getUserId());
		return roleRepository.save(role);
	}

	 public Page<PrivilegeEntity> getPrivilegesByRoleId(Long roleId, int page, int size) {
	        PageRequest pageRequest = PageRequest.of(page, size);
	        return PrivilegeMappingRepository.findPrivilegesByRoleId(roleId, pageRequest);
	    }

}
