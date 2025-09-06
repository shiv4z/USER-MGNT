package in.gov.egramswaraj.serviceimpl;

import java.util.List;

import org.springframework.stereotype.Service;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.entity.UserRoleMappingEntity;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.repo.UserRoleMappingRepository;
import in.gov.egramswaraj.service.UserRoleMappingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRoleMappingServiceImpl implements UserRoleMappingService {

	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserRoleMappingRepository userRoleMappingRepository;

	@Override
	public void assignRolesToUser(Long userId, List<Long> roleIds) {

		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.USER_NOT_FOUND + userId));

		List<RoleEntity> roles = roleRepository.findAllById(roleIds);

		if (roles.size() != roleIds.size()) {
			throw new EntityNotFoundException(AppConstant.SOME_ROLES_NOT_FOUND);
		}

		List<UserRoleMappingEntity> mappings = roles.stream()
				.map(role -> new UserRoleMappingEntity(user, role))
				.toList();

		userRoleMappingRepository.saveAll(mappings);
	}

	@Override
	public void assignUsersToRole(List<Long> userIds, Long roleId) {

		RoleEntity role = roleRepository.findById(roleId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.ROLE_NOT_FOUND + roleId));

		List<UserEntity> users = userRepository.findAllById(userIds);

		if (users.size() != userIds.size()) {
			throw new EntityNotFoundException(AppConstant.SOME_USERS_NOT_FOUND);
		}

		List<UserRoleMappingEntity> mappings = users.stream()
				.map(user -> new UserRoleMappingEntity(user, role))
				.toList();

		userRoleMappingRepository.saveAll(mappings);
	}

}
