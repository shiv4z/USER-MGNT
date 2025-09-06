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
import in.gov.egramswaraj.request.PrivilegeRequest;
import in.gov.egramswaraj.response.PrivilegeResponse;
import in.gov.egramswaraj.service.PrivilegeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/privilege")
public class PrivilegeRestController {

	private final PrivilegeService privilegeService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public PrivilegeResponse createPrivilege(@Valid @RequestBody PrivilegeRequest request) {
		PrivilegeEntity createPrivilege = privilegeService.createPrivilege(request);
		return modelMapper.map(createPrivilege, PrivilegeResponse.class);
	}

	@PatchMapping("/{id}/status/{flag}")
	public ResponseEntity<String> updatePrivilegeStatus(@PathVariable("id") Long PrivilegeId,
			@PathVariable("flag") boolean flag) {
		return ResponseEntity.ok(privilegeService.updatePrivilegeStatus(PrivilegeId, flag));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deletePrivilege(@PathVariable("id") Long id) {
		return ResponseEntity.ok(privilegeService.deletePrivilege(id));
	}

	@GetMapping
	public ResponseEntity<Page<PrivilegeResponse>> getAllUsersPaged(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<PrivilegeEntity> allPrivileges = privilegeService.getAllPrivileges(page, size);
		return ResponseEntity.ok(allPrivileges.map(Privilege -> modelMapper.map(Privilege, PrivilegeResponse.class)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<PrivilegeResponse> getPrivilegeById(@PathVariable("id") Long id) {
		PrivilegeEntity Privilege = privilegeService.getPrivilegeById(id);
		return ResponseEntity.ok(modelMapper.map(Privilege, PrivilegeResponse.class));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<PrivilegeResponse> updatePrivilege(@PathVariable("id") Long id,
			@Valid @RequestBody PrivilegeRequest request) {
		PrivilegeEntity Privilege = privilegeService.updatePrivilege(id, request);
		return ResponseEntity.ok(modelMapper.map(Privilege, PrivilegeResponse.class));
	}
}
