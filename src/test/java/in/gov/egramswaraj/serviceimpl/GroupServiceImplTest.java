package in.gov.egramswaraj.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.GroupEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.repo.GroupRepository;
import in.gov.egramswaraj.repo.GroupRoleMappingRepository;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.repo.UserGroupMappingRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.request.GroupRequest;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {
	@Mock
	private GroupRepository groupRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private UserGroupMappingRepository userGroupMappingRepository;
	@Mock
	private GroupRoleMappingRepository groupRoleMappingRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UserContext userContext;

	@InjectMocks
	private GroupServiceImpl groupServiceImpl;

	private GroupRequest groupRequest;
	private GroupEntity group;

	@BeforeEach
	void setUp() {
		groupRequest = new GroupRequest("TestGroup", "Test Group Description", null, null);

		group = new GroupEntity();
		group.setId(1L);
		group.setGroupName("TestGroup");
		group.setGroupDescription("Test Group Description");
		group.setActive(true);
	}

	@Test
	void testCreateGroup() {
		when(groupRepository.existsByGroupName("TestGroup")).thenReturn(false);
		when(userContext.getUserId()).thenReturn(1L);
		when(modelMapper.map(groupRequest, GroupEntity.class)).thenReturn(group);
		when(groupRepository.save(group)).thenReturn(group);

		GroupEntity created = groupServiceImpl.createGroup(groupRequest);

		assertThat(created).isNotNull();
		assertThat(created.getGroupName()).isEqualTo("TestGroup");
		verify(groupRepository).save(group);
	}

	@Test
	void testCreateGroupAlreadyExists() {
		when(groupRepository.existsByGroupName("TestGroup")).thenReturn(true);

		assertThatThrownBy(() -> groupServiceImpl.createGroup(groupRequest))
				.isInstanceOf(IllegalArgumentException.class).hasMessage(AppConstant.GROUP_ALREADY_EXIST);

		verify(groupRepository, never()).save(any());
	}

	@Test
	void testUpdateGroupStatus() {
		when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
		when(userContext.getUserId()).thenReturn(1L);

		String result = groupServiceImpl.updateGroupStatus(1L, false);

		assertThat(result).isEqualTo(AppConstant.GROUP_STATUS);
		assertThat(group.isActive()).isFalse();
		verify(groupRepository).save(group);
	}

	@Test
	void testUpdateGroupStatusAlreadyInactive() {
		group.setActive(false);
		when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

		String result = groupServiceImpl.updateGroupStatus(1L, false);

		assertThat(result).isEqualTo("Group is already inactive");
		verify(groupRepository, never()).save(any());
	}

	@Test
	void testDeleteGroup() {
		when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

		String result = groupServiceImpl.deleteGroup(1L);

		assertThat(result).isEqualTo(AppConstant.GROUP_DELETION);
		verify(groupRepository).delete(group);
	}

	@Test
	void testGetGroupById() {
		when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

		GroupEntity result = groupServiceImpl.getGroupById(1L);

		assertThat(result).isEqualTo(group);
	}

	@Test
	void testUpdateGroup() {
		when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
		when(userContext.getUserId()).thenReturn(100L);
		when(groupRepository.save(any())).thenReturn(group);

		GroupEntity result = groupServiceImpl.updateGroup(1L, groupRequest);

		assertThat(result).isNotNull();
		verify(modelMapper).map(groupRequest, group);
	}

	@Test
	void testAssignUsersToGroup() {
		UserEntity user1 = new UserEntity();
		user1.setId(101L);
		UserEntity user2 = new UserEntity();
		user2.setId(102L);
		List<UserEntity> users = List.of(user1, user2);

		when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
		when(userRepository.findAllById(List.of(101L, 102L))).thenReturn(users);

		String result = groupServiceImpl.assignUsersToGroup(1L, List.of(101L, 102L));

		assertThat(result).isEqualTo(AppConstant.USER_GROUP_ADDAED);
		verify(userGroupMappingRepository).saveAll(anyList());
	}

	@Test
	void testAssignRolesToGroup() {
		RoleEntity role1 = new RoleEntity();
		role1.setId(11L);
		RoleEntity role2 = new RoleEntity();
		role2.setId(12L);
		List<RoleEntity> roles = List.of(role1, role2);

		when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
		when(roleRepository.findAllById(List.of(11L, 12L))).thenReturn(roles);

		String result = groupServiceImpl.assignRolesToGroup(1L, List.of(11L, 12L));

		assertThat(result).isEqualTo(AppConstant.ROLE_GROUP_ADDAED);
		verify(groupRoleMappingRepository).saveAll(anyList());
	}

	@Test
	void testAssignUsersToGroupUserNotFound() {
		Long groupId = 1L;
		List<Long> invalidUserIds = List.of(999L);

		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(userRepository.findAllById(invalidUserIds)).thenReturn(Collections.emptyList());

		assertThatThrownBy(() -> groupServiceImpl.assignUsersToGroup(groupId, invalidUserIds))
				.isInstanceOf(EntityNotFoundException.class).hasMessage(AppConstant.SOME_USERS_NOT_FOUND);
	}

	@Test
	void testAssignRolesToGroupRoleNotFound() {
		Long groupId = 1L;
		List<Long> invalidRoleIds = List.of(999L);

		when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
		when(roleRepository.findAllById(invalidRoleIds)).thenReturn(Collections.emptyList());

		assertThatThrownBy(() -> groupServiceImpl.assignRolesToGroup(groupId, invalidRoleIds))
				.isInstanceOf(EntityNotFoundException.class).hasMessage(AppConstant.SOME_ROLES_NOT_FOUND);
	}
}
