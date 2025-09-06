package in.gov.egramswaraj.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.request.RoleRequest;
import in.gov.egramswaraj.response.PrivilegeResponse;
import in.gov.egramswaraj.response.RoleResponse;
import in.gov.egramswaraj.service.RoleService;

@ExtendWith(MockitoExtension.class)
class RoleRestControllerTest {

	@InjectMocks
	private RoleRestController roleRestController;

	@Mock
	private RoleService roleService;

	@Mock
	private ModelMapper modelMapper;

	private RoleEntity roleEntity;
	private RoleRequest roleRequest;
	private RoleResponse roleResponse;

	@BeforeEach
	void setUp() {
		roleEntity = new RoleEntity();
		roleEntity.setId(1L);
		roleEntity.setRoleName("Admin");
		roleEntity.setRoleDescription("Administrator Role");

		roleRequest = new RoleRequest("Admin", "Administrator Role", null, null);

		roleResponse = new RoleResponse();
		roleResponse.setId(1L);
		roleResponse.setRoleName("Admin");
		roleResponse.setRoleDescription("Administrator Role");
	}

	@Test
    void testCreateRole() {
        when(roleService.createRole(roleRequest)).thenReturn(roleEntity);
        when(modelMapper.map(roleEntity, RoleResponse.class)).thenReturn(roleResponse);

        RoleResponse response = roleRestController.createRole(roleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getRoleName()).isEqualTo("Admin");
        verify(roleService, times(1)).createRole(roleRequest);
    }

	@Test
    void testUpdateRoleStatus() {
        when(roleService.updateRoleStatus(1L, true)).thenReturn(AppConstant.ROLE_STATUS);

        ResponseEntity<String> response = roleRestController.updateRoleStatus(1L, true);

        assertThat(response.getBody()).isEqualTo(AppConstant.ROLE_STATUS);
        verify(roleService, times(1)).updateRoleStatus(1L, true);
    }

	@Test
    void testDeleteRole() {
        when(roleService.deleteRole(1L)).thenReturn(AppConstant.ROLE_DELETION);

        ResponseEntity<String> response = roleRestController.deleteRole(1L);

        assertThat(response.getBody()).isEqualTo(AppConstant.ROLE_DELETION);
        verify(roleService, times(1)).deleteRole(1L);
    }

	@Test
	void testGetAllUsersPaged() {
		List<RoleEntity> roleList = Collections.singletonList(roleEntity);
		Page<RoleEntity> page = new PageImpl<>(roleList);

		when(roleService.getAllRoles(0, 100)).thenReturn(page);
		when(modelMapper.map(roleEntity, RoleResponse.class)).thenReturn(roleResponse);

		ResponseEntity<Page<RoleResponse>> response = roleRestController.getAllUsersPaged(0, 100);

		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).hasSize(1);
		verify(roleService, times(1)).getAllRoles(0, 100);
	}

	@Test
	void testGetRoleById() {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setId(1L);
		roleEntity.setRoleName("Admin");
		roleEntity.setRoleDescription("Administrator Role");

		when(roleService.getRoleById(1L)).thenReturn(roleEntity);

		RoleResponse roleResponse = new RoleResponse();
		roleResponse.setId(1L);
		roleResponse.setRoleName("Admin");
		roleResponse.setRoleDescription("Administrator Role");

		when(modelMapper.map(roleEntity, RoleResponse.class)).thenReturn(roleResponse);

		ResponseEntity<RoleResponse> response = roleRestController.getRoleById(1L);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getId()).isEqualTo(1L);
		assertThat(response.getBody().getRoleName()).isEqualTo("Admin");
		assertThat(response.getBody().getRoleDescription()).isEqualTo("Administrator Role");

		verify(roleService, times(1)).getRoleById(1L);
		verify(modelMapper, times(1)).map(roleEntity, RoleResponse.class);
	}

	
	@Test
	void testGetPrivilegesByRoleId() {
		PrivilegeEntity resource1 = new PrivilegeEntity();
		resource1.setId(1L);
		resource1.setPrivilegeName("Dashboard");

		PrivilegeEntity resource2 = new PrivilegeEntity();
		resource2.setId(2L);
		resource2.setPrivilegeName("Reports");

		Page<PrivilegeEntity> pagedPrivileges = new PageImpl<>(List.of(resource1, resource2));

		when(roleService.getPrivilegesByRoleId(1L, 0, 100)).thenReturn(pagedPrivileges);

		PrivilegeResponse response1 = new PrivilegeResponse();
		response1.setId(1L);
		response1.setPrivilegeName("Dashboard");

		PrivilegeResponse response2 = new PrivilegeResponse();
		response2.setId(2L);
		response2.setPrivilegeName("Reports");

		when(modelMapper.map(resource1, PrivilegeResponse.class)).thenReturn(response1);
		when(modelMapper.map(resource2, PrivilegeResponse.class)).thenReturn(response2);

		ResponseEntity<Page<PrivilegeResponse>> response = roleRestController.getPrivilegesByRoleId(1L, 0, 100);

		assertThat(response).isNotNull();
		assertThat(response.getStatusCode().value()).isEqualTo(200);

		Page<PrivilegeResponse> body = response.getBody();
		assertThat(body).isNotNull();
		assertThat(body.getContent()).hasSize(2);
		assertThat(body.getContent().get(0).getPrivilegeName()).isEqualTo("Dashboard");
		assertThat(body.getContent().get(1).getPrivilegeName()).isEqualTo("Reports");

		verify(roleService, times(1)).getPrivilegesByRoleId(1L, 0, 100);
		verify(modelMapper, times(1)).map(resource1, PrivilegeResponse.class);
		verify(modelMapper, times(1)).map(resource2, PrivilegeResponse.class);
	}
	
	@Test
    void testUpdateRole() {
        when(roleService.updateRole(1L, roleRequest)).thenReturn(roleEntity);
        when(modelMapper.map(roleEntity, RoleResponse.class)).thenReturn(roleResponse);

        ResponseEntity<RoleResponse> response = roleRestController.updateRole(1L, roleRequest);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getRoleName()).isEqualTo("Admin");
        verify(roleService, times(1)).updateRole(1L, roleRequest);
    }
}
