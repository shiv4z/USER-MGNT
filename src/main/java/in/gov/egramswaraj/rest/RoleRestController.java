package in.gov.egramswaraj.rest;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.request.RoleRequest;
import in.gov.egramswaraj.response.PrivilegeResponse;
import in.gov.egramswaraj.response.RoleResponse;
import in.gov.egramswaraj.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/role")
public class RoleRestController {

	private final RoleService roleService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RoleResponse createRole(@Valid @RequestBody RoleRequest request) {
		RoleEntity role = roleService.createRole(request);
		return modelMapper.map(role, RoleResponse.class);
	}

	@PatchMapping("/{id}/status/{flag}")
	public ResponseEntity<String> updateRoleStatus(@PathVariable("id") Long roleId,
			@PathVariable("flag") boolean flag) {
		return ResponseEntity.ok(roleService.updateRoleStatus(roleId, flag));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteRole(@PathVariable("id") Long id) {
		return ResponseEntity.ok(roleService.deleteRole(id));
	}

	@GetMapping
	public ResponseEntity<Page<RoleResponse>> getAllUsersPaged(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<RoleEntity> allRoles = roleService.getAllRoles(page, size);
		return ResponseEntity.ok(allRoles.map(role -> modelMapper.map(role, RoleResponse.class)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<RoleResponse> getRoleById(@PathVariable("id") Long id) {
		RoleEntity role = roleService.getRoleById(id);
		return ResponseEntity.ok(modelMapper.map(role, RoleResponse.class));
	}

	@GetMapping("/{roleId}/privileges")
	public ResponseEntity<Page<PrivilegeResponse>> getPrivilegesByRoleId(@PathVariable("roleId") Long roleId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<PrivilegeEntity> pagedResources = roleService.getPrivilegesByRoleId(roleId, page, size);
		return ResponseEntity.ok(pagedResources.map(resource -> modelMapper.map(resource, PrivilegeResponse.class)));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<RoleResponse> updateRole(@PathVariable("id") Long id,
			@Valid @RequestBody RoleRequest request) {
		RoleEntity role = roleService.updateRole(id, request);
		return ResponseEntity.ok(modelMapper.map(role, RoleResponse.class));
	}

}
