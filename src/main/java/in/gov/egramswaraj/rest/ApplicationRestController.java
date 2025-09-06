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

import in.gov.egramswaraj.entity.ApplicationEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.request.ApplicationRequest;
import in.gov.egramswaraj.response.ApplicationResponse;
import in.gov.egramswaraj.response.UserResponse;
import in.gov.egramswaraj.service.ApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/application")
public class ApplicationRestController {

	private final ApplicationService applicationService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ApplicationResponse createApplication(@Valid @RequestBody ApplicationRequest request) {
		ApplicationEntity application = applicationService.createApplication(request);
		return modelMapper.map(application, ApplicationResponse.class);
	}

	@PatchMapping("/{id}/status/{flag}")
	public ResponseEntity<String> updateApplicationStatus(@PathVariable("id") Long applicationId,
			@PathVariable("flag") boolean flag) {
		return ResponseEntity.ok(applicationService.updateApplicationStatus(applicationId, flag));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteApplication(@PathVariable("id") Long id) {
		return ResponseEntity.ok(applicationService.deleteApplication(id));
	}

	@GetMapping
	public ResponseEntity<Page<ApplicationResponse>> getAllApplications(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<ApplicationEntity> allApplications = applicationService.getAllApplications(page, size);
		return ResponseEntity.ok(allApplications.map(application -> modelMapper.map(application, ApplicationResponse.class)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApplicationResponse> getApplicationById(@PathVariable("id") Long id) {
		ApplicationEntity application = applicationService.getApplicationById(id);
		return ResponseEntity.ok(modelMapper.map(application, ApplicationResponse.class));
	}
	
	@GetMapping("/{applicationId}/users")
	public ResponseEntity<Page<UserResponse>> getUsersByApplicationId(@PathVariable("applicationId") Long applicationId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<UserEntity> pagedUser = applicationService.getUsersByApplicationId(applicationId, page, size);
		return ResponseEntity.ok(pagedUser.map(user -> modelMapper.map(user, UserResponse.class)));
	}
	
	

	@PatchMapping("/{id}")
	public ResponseEntity<ApplicationResponse> updateApplication(@PathVariable("id") Long id,
			@Valid @RequestBody ApplicationRequest request) {
		ApplicationEntity application = applicationService.updateApplication(id, request);
		return ResponseEntity.ok(modelMapper.map(application, ApplicationResponse.class));
	}
}
	