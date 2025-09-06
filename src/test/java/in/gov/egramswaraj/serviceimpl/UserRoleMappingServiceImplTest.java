package in.gov.egramswaraj.serviceimpl;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.repo.UserRoleMappingRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserRoleMappingServiceImplTest {
	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private UserRoleMappingRepository userRoleMappingRepository;

	@InjectMocks
	private UserRoleMappingServiceImpl userRoleMappingServiceImpl;

	private UserEntity user;
	private RoleEntity role;

	@BeforeEach
	void setUp() {
		user = new UserEntity();
		user.setId(1L);
		user.setUsername("testuser");

		role = new RoleEntity();
		role.setId(10L);
		role.setRoleName("ADMIN");
	}

	@Test
	void testAssignRolesToUser() {
		List<Long> roleIds = List.of(10L, 11L);
		RoleEntity role1 = new RoleEntity();
		role1.setId(10L);
		RoleEntity role2 = new RoleEntity();
		role2.setId(11L);

		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
		when(roleRepository.findAllById(roleIds)).thenReturn(List.of(role1, role2));

		userRoleMappingServiceImpl.assignRolesToUser(1L, roleIds);

		verify(userRoleMappingRepository).saveAll(anyList());
	}

	@Test
	void testAssignRolesToUserNotFound() {
		Long userId = 1L;
		List<Long> ids = List.of(10L);
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userRoleMappingServiceImpl.assignRolesToUser(userId, ids))
				.isInstanceOf(EntityNotFoundException.class).hasMessageContaining(AppConstant.USER_NOT_FOUND + userId);
	}

	@Test
	void testAssignRolesToUserSomeRolesNotFound() {
		Long userId = 1L;
		List<Long> ids = List.of(10L, 11L);

		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(roleRepository.findAllById(ids)).thenReturn(List.of(new RoleEntity()));

		assertThatThrownBy(() -> userRoleMappingServiceImpl.assignRolesToUser(userId, ids))
				.isInstanceOf(EntityNotFoundException.class).hasMessageContaining(AppConstant.SOME_ROLES_NOT_FOUND);
	}

	@Test
	void testAssignUsersToRole() {
		List<Long> userIds = List.of(1L, 2L);
		UserEntity user1 = new UserEntity();
		user1.setId(1L);
		UserEntity user2 = new UserEntity();
		user2.setId(2L);

		when(roleRepository.findById(10L)).thenReturn(Optional.of(role));
		when(userRepository.findAllById(userIds)).thenReturn(List.of(user1, user2));

		userRoleMappingServiceImpl.assignUsersToRole(userIds, 10L);

		verify(userRoleMappingRepository).saveAll(anyList());
	}

	@Test
	void testAssignUsersToRoleNotFound() {
		Long roleId = 10L;
		List<Long> of = List.of(1L, 2L);
		when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userRoleMappingServiceImpl.assignUsersToRole(of, roleId))
				.isInstanceOf(EntityNotFoundException.class).hasMessageContaining(AppConstant.ROLE_NOT_FOUND + roleId);
	}

	@Test
	void testAssignUsersToRoleSomeUsersNotFound() {
		Long roleId = 10L;
		List<Long> of = List.of(1L, 2L);
		when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
		when(userRepository.findAllById(of)).thenReturn(List.of(new UserEntity()));

		assertThatThrownBy(() -> userRoleMappingServiceImpl.assignUsersToRole(of, roleId))
				.isInstanceOf(EntityNotFoundException.class).hasMessageContaining(AppConstant.SOME_USERS_NOT_FOUND);
	}
}
