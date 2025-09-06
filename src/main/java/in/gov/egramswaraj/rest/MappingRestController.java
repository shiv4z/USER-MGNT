package in.gov.egramswaraj.rest;

import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.request.ApplicationMappingRequest;
import in.gov.egramswaraj.request.PrivilegeRoleMappingRequest;
import in.gov.egramswaraj.request.UserRoleMappingRequest;
import in.gov.egramswaraj.service.ApplicationService;
import in.gov.egramswaraj.service.PrivilegeMappingService;
import in.gov.egramswaraj.service.UserRoleMappingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mapping")
public class MappingRestController {

	private final UserRoleMappingService userRoleMappingService;
	private final PrivilegeMappingService privilegeMappingService;
	private final ApplicationService applicationService;

	@PostMapping("/userrole")
	public ResponseEntity<String> mapUserRoles(@Valid @RequestBody UserRoleMappingRequest request)
			throws BadRequestException {
		if (null != request.getUserId()) {
			userRoleMappingService.assignRolesToUser(request.getUserId(), request.getIds());
			return ResponseEntity.ok(AppConstant.ROLE_MAPPED);
		} else if (null != request.getRoleId()) {
			userRoleMappingService.assignUsersToRole(request.getIds(), request.getRoleId());
			return ResponseEntity.ok(AppConstant.USER_MAPPED);
		} else {
			throw new BadRequestException(AppConstant.BAD_REQUEST_MSG);
		}
	}

	@PostMapping("/privilegesrole")
	public ResponseEntity<String> mapResourcesToRole(@Valid @RequestBody PrivilegeRoleMappingRequest request) {
		return ResponseEntity
				.ok(privilegeMappingService.mapPrivilegesToRole(request.getRoleId(), request.getResourceIds()));
	}

	@PostMapping("/applicationusers")
	public String mapUsersToApplication(@RequestBody ApplicationMappingRequest request) {
		return applicationService.mapUsersToApplication(request.getApplicationId(), request.getUserIds(), request.getGroupId());
	}
}
