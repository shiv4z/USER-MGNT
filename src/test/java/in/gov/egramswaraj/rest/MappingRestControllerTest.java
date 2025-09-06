package in.gov.egramswaraj.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.request.ApplicationMappingRequest;
import in.gov.egramswaraj.request.PrivilegeRoleMappingRequest;
import in.gov.egramswaraj.request.UserRoleMappingRequest;
import in.gov.egramswaraj.service.ApplicationService;
import in.gov.egramswaraj.service.PrivilegeMappingService;
import in.gov.egramswaraj.service.UserRoleMappingService;

@ExtendWith(MockitoExtension.class)
class MappingRestControllerTest {

	@InjectMocks
	private MappingRestController mappingRestController;

	@Mock
	private UserRoleMappingService userRoleMappingService;

	@Mock
	private PrivilegeMappingService privilegeMappingService;

	@Mock
	private ApplicationService applicationService;

	@Test
	void testMapUserRoles_WithUserId() throws BadRequestException {
		UserRoleMappingRequest request = new UserRoleMappingRequest(100L, null, Arrays.asList(1L, 2L));

		doNothing().when(userRoleMappingService).assignRolesToUser(100L, request.getIds());

		ResponseEntity<String> response = mappingRestController.mapUserRoles(request);

		assertThat(response.getBody()).isEqualTo(AppConstant.ROLE_MAPPED);
		verify(userRoleMappingService, times(1)).assignRolesToUser(100L, request.getIds());
	}

	@Test
	void testMapUserRoles_WithRoleId() throws BadRequestException {
		UserRoleMappingRequest request = new UserRoleMappingRequest(null, 10L, Arrays.asList(1L, 2L));

		doNothing().when(userRoleMappingService).assignUsersToRole(request.getIds(), 10L);

		ResponseEntity<String> response = mappingRestController.mapUserRoles(request);

		assertThat(response.getBody()).isEqualTo(AppConstant.USER_MAPPED);
		verify(userRoleMappingService, times(1)).assignUsersToRole(request.getIds(), 10L);
	}

	@Test
	void testMapUserRoles_InvalidRequest_ShouldThrowBadRequest() {
		UserRoleMappingRequest request = new UserRoleMappingRequest(null, null, Arrays.asList(1L, 2L));

		Exception exception = assertThrows(BadRequestException.class, () -> {
			mappingRestController.mapUserRoles(request);
		});

		assertThat(exception.getMessage()).isEqualTo(AppConstant.BAD_REQUEST_MSG);
		verifyNoInteractions(userRoleMappingService);
	}

	@Test
	void testMapResourcesToRole() {
		PrivilegeRoleMappingRequest request = new PrivilegeRoleMappingRequest(10L, Arrays.asList(1L, 2L));

		when(privilegeMappingService.mapPrivilegesToRole(10L, request.getResourceIds()))
				.thenReturn(AppConstant.PRIVILEGE_ROLE_MAPPING);

		ResponseEntity<String> response = mappingRestController.mapResourcesToRole(request);

		assertThat(response.getBody()).isEqualTo(AppConstant.PRIVILEGE_ROLE_MAPPING);
		verify(privilegeMappingService, times(1)).mapPrivilegesToRole(10L, request.getResourceIds());
	}

	@Test
	void testMapUsersToApplication() {
		ApplicationMappingRequest request = new ApplicationMappingRequest(5L, Arrays.asList(1L, 2L), 1L);

		when(applicationService.mapUsersToApplication(5L, request.getUserIds(), 1L))
				.thenReturn(AppConstant.APPLICATION_MAPPING_STATUS);

		String result = mappingRestController.mapUsersToApplication(request);

		assertThat(result).isEqualTo(AppConstant.APPLICATION_MAPPING_STATUS);
		verify(applicationService, times(1)).mapUsersToApplication(5L, request.getUserIds(), 1L);
	}
}