package in.gov.egramswaraj.service;

import java.util.List;

import org.springframework.data.domain.Page;

import in.gov.egramswaraj.entity.GroupEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.request.GroupRequest;
import jakarta.validation.Valid;

public interface GroupService {

	GroupEntity createGroup(@Valid GroupRequest request);

	String updateGroupStatus(Long groupId, boolean flag);
	
	String deleteGroup(Long id);

	Page<GroupEntity> getAllGroups(int page, int size);

	GroupEntity getGroupById(Long id);

	GroupEntity updateGroup(Long id, GroupRequest request);

	String assignUsersToGroup(Long groupId, List<Long> userIds);

	String assignRolesToGroup(Long groupId, List<Long> roleIds);

	//shiv addition
	Page<UserEntity> getUsersByGroupId(Long groupId, int page, int size);

	Page<RoleEntity> getRoledByGroupId(Long groupId, int page, int size);

	

}
