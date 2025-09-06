package in.gov.egramswaraj.rest;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import in.gov.egramswaraj.entity.UserProfileEntity;
import in.gov.egramswaraj.request.UserProfileRequest;
import in.gov.egramswaraj.response.UserProfileResponse;
import in.gov.egramswaraj.service.ProfileService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class UserProfileRestController {

	private final ProfileService profileService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserProfileResponse createUserProfile(@Valid @RequestBody UserProfileRequest request) {
		UserProfileEntity profile = profileService.createUserProfile(request);
		return modelMapper.map(profile, UserProfileResponse.class);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserProfileResponse> getUserProfileById(
			@PathVariable("id") @Min(value = 1, message = "ID must be greater than 0") Long id) {

		UserProfileEntity profile = profileService.getUserProfileById(id);
		return ResponseEntity.ok(setUserId(profile));
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UserProfileResponse> updateUserProfile(@PathVariable(	"id") @Min(1) Long id,
			@Valid @RequestBody UserProfileRequest request) {

		UserProfileEntity profile = profileService.updateUserProfile(id, request);
		return ResponseEntity.ok(setUserId(profile));
	}

	private UserProfileResponse setUserId(UserProfileEntity profile) {
		UserProfileResponse response = modelMapper.map(profile, UserProfileResponse.class);
		if (null != profile.getUser()) {
			response.setUserId(profile.getUser().getId());
		}
		return response;
	}

}
