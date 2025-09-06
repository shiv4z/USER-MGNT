package in.gov.egramswaraj.rest;

import org.apache.coyote.BadRequestException;
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

import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.request.HierarchyRequest;
import in.gov.egramswaraj.request.PasswordRequest;
import in.gov.egramswaraj.request.UserRequest;
import in.gov.egramswaraj.response.UserDeatilsResponse;
import in.gov.egramswaraj.response.UserResponse;
import in.gov.egramswaraj.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserRestController {

	private final UserService userService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserResponse createUser(@Valid @RequestBody UserRequest request) {
		UserEntity user = userService.createUser(request);
		return modelMapper.map(user, UserResponse.class);
	}

	@PatchMapping("/{id}/status/{flag}")
	public ResponseEntity<String> updateUserStatus(@PathVariable("id") Long userId,
			@PathVariable("flag") boolean flag) {
		return ResponseEntity.ok(userService.updateUserStatus(userId, flag));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
		return ResponseEntity.ok(userService.deleteUser(id));
	}

	@GetMapping
	public ResponseEntity<Page<UserResponse>> getAllUsersPaged(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<UserEntity> allUsers = userService.getAllUsers(page, size);
		return ResponseEntity.ok(allUsers.map(user -> modelMapper.map(user, UserResponse.class)));
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDeatilsResponse> getUserById(@PathVariable("id") Long id) {
		UserEntity user = userService.getUserById(id);
		return ResponseEntity.ok(modelMapper.map(user, UserDeatilsResponse.class));
	}

//	@PatchMapping("/{id}")
//	public ResponseEntity<UserResponse> updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserRequest request) {
//		UserEntity user = userService.updateUser(id, request);
//		return ResponseEntity.ok(modelMapper.map(user, UserResponse.class));
//	}
	
	@PatchMapping("/{id}/hierarchy")
	public ResponseEntity<String> updateHierarchy(@PathVariable("id") Long id, @Valid @RequestBody HierarchyRequest request)  {
		return ResponseEntity.ok(userService.updateHierarchy(id, request));
	}

	@PatchMapping("/{id}/password")
	public ResponseEntity<String> updatePassword(@PathVariable("id") Long id, @Valid @RequestBody PasswordRequest request) throws BadRequestException {
		return ResponseEntity.ok(userService.updatePassword(id, request));
	}
}
