package in.gov.egramswaraj.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

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
import in.gov.egramswaraj.request.PrivilegeRequest;
import in.gov.egramswaraj.response.PrivilegeResponse;
import in.gov.egramswaraj.service.PrivilegeService;

@ExtendWith(MockitoExtension.class)
class PrivilegeRestControllerTest {

	@InjectMocks
	private PrivilegeRestController controller;

	@Mock
	private PrivilegeService resourceService;

	@Mock
	private ModelMapper modelMapper;

	private PrivilegeEntity entity;
	private PrivilegeRequest request;
	private PrivilegeResponse responseDto;

	@BeforeEach
	void setUp() {
		entity = new PrivilegeEntity();
		entity.setId(1L);
		entity.setPrivilegeName("Dashboard");
		entity.setPrivilegeUrl("/dashboard");
		entity.setDescription("Main dashboard");

		request = new PrivilegeRequest("Dashboard", "/dashboard", "Main dashboard", null, null);

		responseDto = new PrivilegeResponse();
		responseDto.setId(1L);
		responseDto.setPrivilegeName("Dashboard");
		responseDto.setPrivilegeUrl("/dashboard");
		responseDto.setDescription("Main dashboard");
	}

	@Test
    void shouldCreatePrivilege() {
        when(resourceService.createPrivilege(request)).thenReturn(entity);
        when(modelMapper.map(entity, PrivilegeResponse.class)).thenReturn(responseDto);

        PrivilegeResponse response = controller.createPrivilege(request);

        assertThat(response).isNotNull();
        assertThat(response.getPrivilegeName()).isEqualTo("Dashboard");
        verify(resourceService).createPrivilege(request);
    }

	@Test
    void shouldUpdatePrivilegeStatus() {
        when(resourceService.updatePrivilegeStatus(1L, true)).thenReturn(AppConstant.PRIVILEGE_STATUS);

        ResponseEntity<String> response = controller.updatePrivilegeStatus(1L, true);

        assertThat(response.getBody()).isEqualTo(AppConstant.PRIVILEGE_STATUS);
        verify(resourceService).updatePrivilegeStatus(1L, true);
    }

	@Test
    void shouldDeletePrivilege() {
        when(resourceService.deletePrivilege(1L)).thenReturn(AppConstant.PRIVILEGE_DELETION);

        ResponseEntity<String> response = controller.deletePrivilege(1L);

        assertThat(response.getBody()).isEqualTo(AppConstant.PRIVILEGE_DELETION);
        verify(resourceService).deletePrivilege(1L);
    }

	@Test
	void shouldGetAllPrivilegesPaged() {
		Page<PrivilegeEntity> page = new PageImpl<>(Collections.singletonList(entity));
		when(resourceService.getAllPrivileges(0, 100)).thenReturn(page);
		when(modelMapper.map(entity, PrivilegeResponse.class)).thenReturn(responseDto);

		ResponseEntity<Page<PrivilegeResponse>> response = controller.getAllUsersPaged(0, 100);

		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getContent()).hasSize(1);
		assertThat(response.getBody().getContent().get(0).getPrivilegeName()).isEqualTo("Dashboard");
		verify(resourceService).getAllPrivileges(0, 100);
	}

	@Test
    void shouldGetPrivilegeById() {
        when(resourceService.getPrivilegeById(1L)).thenReturn(entity);
        when(modelMapper.map(entity, PrivilegeResponse.class)).thenReturn(responseDto);

        ResponseEntity<PrivilegeResponse> response = controller.getPrivilegeById(1L);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPrivilegeName()).isEqualTo("Dashboard");
        verify(resourceService).getPrivilegeById(1L);
    }

	@Test
    void shouldUpdatePrivilege() {
        when(resourceService.updatePrivilege(1L, request)).thenReturn(entity);
        when(modelMapper.map(entity, PrivilegeResponse.class)).thenReturn(responseDto);

        ResponseEntity<PrivilegeResponse> response = controller.updatePrivilege(1L, request);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getPrivilegeUrl()).isEqualTo("/dashboard");
        verify(resourceService).updatePrivilege(1L, request);
    }
}
