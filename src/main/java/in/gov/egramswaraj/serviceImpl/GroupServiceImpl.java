package in.gov.egramswaraj.serviceimpl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.GroupEntity;
import in.gov.egramswaraj.entity.GroupRoleMappingEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.entity.UserGroupMappingEntity;
import in.gov.egramswaraj.repo.GroupRepository;
import in.gov.egramswaraj.repo.GroupRoleMappingRepository;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.repo.UserGroupMappingRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.request.GroupRequest;
import in.gov.egramswaraj.service.GroupService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

	private final GroupRepository groupRepository;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final UserGroupMappingRepository userGroupMappingRepository;
	private final GroupRoleMappingRepository groupRoleMappingRepository;
	private final ModelMapper modelMapper;
	private final UserContext userContext;

	@Override
	public GroupEntity createGroup(@Valid GroupRequest request) {

		if (groupRepository.existsByGroupName(request.getGroupName())) {
			throw new IllegalArgumentException(AppConstant.GROUP_ALREADY_EXIST);
		}
		request.setCreatedBy(userContext.getUserId());
		GroupEntity group = modelMapper.map(request, GroupEntity.class);
		return groupRepository.save(group);
	}

	@Override
	public String updateGroupStatus(Long groupId, boolean flag) {
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.GROUP_NOT_FOUND + groupId));

		if (group.isActive() == flag) {
			return "Group is already " + (flag ? "active" : "inactive");
		}

		group.setActive(false);
		group.setUpdatedBy(userContext.getUserId());
		groupRepository.save(group);
		return AppConstant.GROUP_STATUS;
	}

	@Override
	public String deleteGroup(Long groupId) {
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.GROUP_NOT_FOUND + groupId));

		groupRepository.delete(group);
		return AppConstant.GROUP_DELETION;
	}

	@Override
	public Page<GroupEntity> getAllGroups(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
		return groupRepository.findAll(pageable);
	}

	@Override
	public GroupEntity getGroupById(Long id) {
		return groupRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.GROUP_NOT_FOUND + id));
	}

	@Override
	public GroupEntity updateGroup(Long groupId, GroupRequest request) {
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.GROUP_NOT_FOUND + groupId));
		request.setUpdatedBy(userContext.getUserId());
		modelMapper.map(request, group);
		return groupRepository.save(group);
	}

	@Override
	public String assignUsersToGroup(Long groupId, List<Long> userIds) {
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.GROUP_NOT_FOUND + groupId));

		List<UserEntity> users = userRepository.findAllById(userIds);
		if (users.size() != userIds.size()) {
			throw new EntityNotFoundException(AppConstant.SOME_USERS_NOT_FOUND);
		}

		List<UserGroupMappingEntity> mappings = users.stream().map(user -> {
			UserGroupMappingEntity mapping = new UserGroupMappingEntity();
			mapping.setGroup(group);
			mapping.setUser(user);

//			UserGroupId id = new UserGroupId();
//			id.setUserId(user.getId());
//			id.setGroupId(group.getId());
//			mapping.setId(id);
			return mapping;
		}).toList();

		userGroupMappingRepository.saveAll(mappings);
		return AppConstant.USER_GROUP_ADDAED;
	}

	@Override
	public String assignRolesToGroup(Long groupId, List<Long> roleIds) {
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.GROUP_NOT_FOUND + groupId));

		List<RoleEntity> roles = roleRepository.findAllById(roleIds);
		if (roles.size() != roleIds.size()) {
			throw new EntityNotFoundException(AppConstant.SOME_ROLES_NOT_FOUND);
		}

		List<GroupRoleMappingEntity> mappings = roles.stream().map(role -> {
			GroupRoleMappingEntity mapping = new GroupRoleMappingEntity();
			mapping.setGroup(group);
			mapping.setRole(role);
//
//			GroupRoleId id = new GroupRoleId();
//			id.setRoleId(role.getId());
//			id.setGroupId(group.getId());
//			mapping.setId(id);

			return mapping;
		}).toList();

		groupRoleMappingRepository.saveAll(mappings);
		return AppConstant.ROLE_GROUP_ADDAED;
	}

	
	//modified by shiv for pagination
	@Override
	public Page<UserEntity> getUsersByGroupId(Long groupId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return userGroupMappingRepository.findUsersByGroupId(groupId, pageRequest);
    }

	@Override
	public Page<RoleEntity> getRoledByGroupId(Long groupId, int page, int size) {
		PageRequest pageRequest = PageRequest.of(page, size);
		return groupRoleMappingRepository.findRolesByGroupId(groupId, pageRequest);
	}

}
