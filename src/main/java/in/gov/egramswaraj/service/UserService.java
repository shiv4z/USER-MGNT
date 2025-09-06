package in.gov.egramswaraj.service;

import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;

import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.request.HierarchyRequest;
import in.gov.egramswaraj.request.PasswordRequest;
import in.gov.egramswaraj.request.UserRequest;
import jakarta.validation.Valid;

public interface UserService {

	UserEntity createUser(UserRequest request);

	String updateUserStatus(Long userId, boolean flag);

	String deleteUser(Long userId);

	Page<UserEntity> getAllUsers(int page, int size);

	UserEntity getUserById(Long id);

	//UserEntity updateUser(Long id, @Valid UserRequest request);

	String updatePassword(Long id, @Valid PasswordRequest request) throws BadRequestException;

	String updateHierarchy(Long id, @Valid HierarchyRequest request);

}
