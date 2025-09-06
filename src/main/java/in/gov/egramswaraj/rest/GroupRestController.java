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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/group")
public class GroupRestController {

	private final GroupService groupService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public GroupResponse createGroup(@Valid @RequestBody GroupRequest request) {
		GroupEntity group = groupService.createGroup(request);
		return modelMapper.map(group, GroupResponse.class);
	}

	@PatchMapping("/{id}/status/{flag}")
	public ResponseEntity<String> updateGroupStatus(@PathVariable("id") Long groupId,
			@PathVariable("flag") boolean flag) {
		return ResponseEntity.ok(groupService.updateGroupStatus(groupId, flag));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteGroup(@PathVariable("id") Long id) {
		return ResponseEntity.ok(groupService.deleteGroup(id));
	}

	@GetMapping
	public ResponseEntity<Page<GroupResponse>> getAllGroupsPaged(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<GroupEntity> allGroups = groupService.getAllGroups(page, size);
		return ResponseEntity.ok(allGroups.map(group -> modelMapper.map(group, GroupResponse.class)));
	}


	@GetMapping("/{id}")
	public ResponseEntity<GroupResponse> getGroupById(@PathVariable("id") Long id) {
		GroupEntity group = groupService.getGroupById(id);
		return ResponseEntity.ok(modelMapper.map(group, GroupResponse.class));
	}

	@GetMapping("/{groupId}/users")
	public ResponseEntity<Page<UserResponse>> getUsersByGroupId(@PathVariable("groupId") Long groupId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<UserEntity> pagedUser = groupService.getUsersByGroupId(groupId, page, size);
		return ResponseEntity.ok(pagedUser.map(user -> modelMapper.map(user, UserResponse.class)));
	}

	@GetMapping("/{groupId}/roles")
	public ResponseEntity<Page<RoleResponse>> getRolesByGroupId(@PathVariable("groupId") Long groupId,
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "100") int size) {
		Page<RoleEntity> pagedRoles = groupService.getRoledByGroupId(groupId, page, size);
		return ResponseEntity.ok(pagedRoles.map(role -> modelMapper.map(role, RoleResponse.class)));
	}
	
	
	@PatchMapping("/{id}")
	public ResponseEntity<GroupResponse> updateGroup(@PathVariable("id") Long id,
			@Valid @RequestBody GroupRequest request) {
		GroupEntity group = groupService.updateGroup(id, request);
		return ResponseEntity.ok(modelMapper.map(group, GroupResponse.class));
	}

	@PostMapping("/addusers")
	public ResponseEntity<String> mapUsersToGroup(@Valid @RequestBody UserGroupMappingRequest request) {
		return ResponseEntity.ok(groupService.assignUsersToGroup(request.getGroupId(), request.getUserIds()));
	}

	@PostMapping("/addgrouproles")
	public ResponseEntity<String> mapRolesToGroup(@Valid @RequestBody GroupRoleMappingRequest request) {
		return ResponseEntity.ok(groupService.assignRolesToGroup(request.getGroupId(), request.getRoleIds()));
	}

}
