package in.gov.egramswaraj.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.entity.GroupEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.request.GroupRequest;
import in.gov.egramswaraj.request.GroupRoleMappingRequest;
import in.gov.egramswaraj.request.UserGroupMappingRequest;
import in.gov.egramswaraj.response.GroupResponse;
import in.gov.egramswaraj.response.RoleResponse;
import in.gov.egramswaraj.response.UserResponse;
import in.gov.egramswaraj.service.GroupService;

@ExtendWith(MockitoExtension.class)
class GroupRestControllerTest {

	@InjectMocks
	private GroupRestController controller;

	@Mock
	private GroupService groupService;

	@Mock
	private ModelMapper modelMapper;

	private GroupEntity groupEntity;
	private GroupRequest groupRequest;
	private GroupResponse groupResponse;

	@BeforeEach
	void setUp() {
		groupEntity = new GroupEntity();
		groupEntity.setId(1L);
		groupEntity.setGroupName("AdminGroup");
		groupEntity.setGroupDescription("Admins");

		groupRequest = new GroupRequest("AdminGroup", "Admins", null, null);

		groupResponse = new GroupResponse();
		groupResponse.setId(1L);
		groupResponse.setGroupName("AdminGroup");
		groupResponse.setGroupDescription("Admins");
	}

	@Test
    void shouldCreateGroup() {
        when(groupService.createGroup(groupRequest)).thenReturn(groupEntity);
        when(modelMapper.map(groupEntity, GroupResponse.class)).thenReturn(groupResponse);

        GroupResponse response = controller.createGroup(groupRequest);

        assertThat(response).isNotNull().extracting(GroupResponse::getGroupName).isEqualTo("AdminGroup");
        verify(groupService).createGroup(groupRequest);
    }

	@Test
    void shouldUpdateGroupStatus() {
        when(groupService.updateGroupStatus(1L, true)).thenReturn(AppConstant.GROUP_STATUS);

        ResponseEntity<String> response = controller.updateGroupStatus(1L, true);

        assertThat(response.getBody()).isEqualTo(AppConstant.GROUP_STATUS);
        verify(groupService).updateGroupStatus(1L, true);
    }

	@Test
    void shouldDeleteGroup() {
        when(groupService.deleteGroup(1L)).thenReturn(AppConstant.GROUP_DELETION);

        ResponseEntity<String> response = controller.deleteGroup(1L);

        assertThat(response.getBody()).isEqualTo(AppConstant.GROUP_DELETION);
        verify(groupService).deleteGroup(1L);
    }

	@Test
	void shouldGetAllGroupsPaged() {
		Page<GroupEntity> page = new PageImpl<>(Collections.singletonList(groupEntity));
		when(groupService.getAllGroups(0, 100)).thenReturn(page);
		when(modelMapper.map(groupEntity, GroupResponse.class)).thenReturn(groupResponse);

		ResponseEntity<Page<GroupResponse>> response = controller.getAllGroupsPaged(0, 100);

		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).hasSize(1);
		assertThat(response.getBody().getContent().get(0).getGroupName()).isEqualTo("AdminGroup");
	}

	@Test
	void shouldGetGroupById() {
		GroupEntity groupEntity = new GroupEntity();
		groupEntity.setId(1L);
		groupEntity.setGroupName("AdminGroup");
		groupEntity.setGroupDescription("Admins");

		when(groupService.getGroupById(1L)).thenReturn(groupEntity);

		GroupResponse groupResponse = new GroupResponse();
		groupResponse.setId(1L);
		groupResponse.setGroupName("AdminGroup");
		groupResponse.setGroupDescription("Admins");

		when(modelMapper.map(groupEntity, GroupResponse.class)).thenReturn(groupResponse);

		ResponseEntity<GroupResponse> response = controller.getGroupById(1L);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isEqualTo(1L);
		assertThat(response.getBody().getGroupName()).isEqualTo("AdminGroup");
		assertThat(response.getBody().getGroupDescription()).isEqualTo("Admins");

		verify(groupService, times(1)).getGroupById(1L);
		verify(modelMapper, times(1)).map(groupEntity, GroupResponse.class);
	}

	@Test
	void shouldGetUsersByGroupId() {
		UserEntity user1 = new UserEntity();
		user1.setId(101L);
		user1.setUsername("john");

		UserEntity user2 = new UserEntity();
		user2.setId(102L);
		user2.setUsername("alice");

		Page<UserEntity> pagedUsers = new PageImpl<>(List.of(user1, user2));

		when(groupService.getUsersByGroupId(1L, 0, 100)).thenReturn(pagedUsers);

		UserResponse ur1 = new UserResponse();
		ur1.setId(101L);
		ur1.setUsername("john");

		UserResponse ur2 = new UserResponse();
		ur2.setId(102L);
		ur2.setUsername("alice");

		when(modelMapper.map(user1, UserResponse.class)).thenReturn(ur1);
		when(modelMapper.map(user2, UserResponse.class)).thenReturn(ur2);

		ResponseEntity<Page<UserResponse>> response = controller.getUsersByGroupId(1L, 0, 100);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).hasSize(2);
		assertThat(response.getBody().getContent().get(0).getUsername()).isEqualTo("john");
		assertThat(response.getBody().getContent().get(1).getUsername()).isEqualTo("alice");

		verify(groupService, times(1)).getUsersByGroupId(1L, 0, 100);
		verify(modelMapper, times(1)).map(user1, UserResponse.class);
		verify(modelMapper, times(1)).map(user2, UserResponse.class);
	}

	@Test
	void shouldGetRolesByGroupId() {
		RoleEntity role1 = new RoleEntity();
		role1.setId(201L);
		role1.setRoleName("ADMIN");

		RoleEntity role2 = new RoleEntity();
		role2.setId(202L);
		role2.setRoleName("USER");

		Page<RoleEntity> pagedRoles = new PageImpl<>(List.of(role1, role2));

		when(groupService.getRoledByGroupId(1L, 0, 100)).thenReturn(pagedRoles);

		RoleResponse rr1 = new RoleResponse();
		rr1.setId(201L);
		rr1.setRoleName("ADMIN");

		RoleResponse rr2 = new RoleResponse();
		rr2.setId(202L);
		rr2.setRoleName("USER");

		when(modelMapper.map(role1, RoleResponse.class)).thenReturn(rr1);
		when(modelMapper.map(role2, RoleResponse.class)).thenReturn(rr2);

		ResponseEntity<Page<RoleResponse>> response = controller.getRolesByGroupId(1L, 0, 100);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).hasSize(2);
		assertThat(response.getBody().getContent().get(0).getRoleName()).isEqualTo("ADMIN");
		assertThat(response.getBody().getContent().get(1).getRoleName()).isEqualTo("USER");

		verify(groupService, times(1)).getRoledByGroupId(1L, 0, 100);
		verify(modelMapper, times(1)).map(role1, RoleResponse.class);
		verify(modelMapper, times(1)).map(role2, RoleResponse.class);
	}

	@Test
    void shouldUpdateGroup() {
        when(groupService.updateGroup(1L, groupRequest)).thenReturn(groupEntity);
        when(modelMapper.map(groupEntity, GroupResponse.class)).thenReturn(groupResponse);

        ResponseEntity<GroupResponse> response = controller.updateGroup(1L, groupRequest);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getGroupName()).isEqualTo("AdminGroup");
    }

	@Test
	void shouldMapUsersToGroup() {
		UserGroupMappingRequest request = new UserGroupMappingRequest(1L, Arrays.asList(101L, 102L));
		when(groupService.assignUsersToGroup(1L, request.getUserIds())).thenReturn("Users mapped successfully");

		ResponseEntity<String> response = controller.mapUsersToGroup(request);

		assertThat(response.getBody()).isEqualTo("Users mapped successfully");
		verify(groupService).assignUsersToGroup(1L, request.getUserIds());
	}

	@Test
	void shouldMapRolesToGroup() {
		GroupRoleMappingRequest request = new GroupRoleMappingRequest(1L, Arrays.asList(201L, 202L));
		when(groupService.assignRolesToGroup(1L, request.getRoleIds())).thenReturn("Roles mapped successfully");

		ResponseEntity<String> response = controller.mapRolesToGroup(request);

		assertThat(response.getBody()).isEqualTo("Roles mapped successfully");
		verify(groupService).assignRolesToGroup(1L, request.getRoleIds());
	}
}