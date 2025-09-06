package in.gov.egramswaraj.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.CredentialEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.entity.UserHierarchyEntity;
import in.gov.egramswaraj.repo.CredentialRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.request.UserRequest;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

	@Mock
	private CredentialRepository credentialRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private PasswordEncoder passwordEncoder;
	@Mock
	private UserContext userContext;

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	private UserRequest request;
	private UserEntity user;

	@BeforeEach
	void setUp() {
		request = new UserRequest();
		request.setUsername("shiv.prasad");
		request.setPassword("Shiv@123");
		request.setDescription("writer");
		request.setLocalbodyCode(50001);
		request.setVersion(3);

		user = new UserEntity();
		user.setId(1L);
		user.setUsername(request.getUsername());
		user.setActive(true);
		user.setCredentials(new HashSet<>());
	}

	@Test
	void testAlreadyExistUserByUsername() {
		when(userRepository.existsByUsername("shiv.prasad")).thenReturn(true);

		assertThatThrownBy(() -> userServiceImpl.createUser(request)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage(AppConstant.USER_ALREADY_EXIST);

		verify(userRepository, never()).save(any());
	}

	@Test
	void testCreateUser() {
		when(userRepository.existsByUsername("shiv.prasad")).thenReturn(false);
		when(userContext.getUserId()).thenReturn(1L);
		when(passwordEncoder.encode("Shiv@123")).thenReturn("password-encoded");
		when(modelMapper.map(request, UserEntity.class)).thenReturn(user);
		CredentialEntity credential = new CredentialEntity();
		when(modelMapper.map(request, CredentialEntity.class)).thenReturn(credential);
		UserHierarchyEntity hierarchy = new UserHierarchyEntity();
		when(modelMapper.map(request, UserHierarchyEntity.class)).thenReturn(hierarchy);

		when(userRepository.save(any(UserEntity.class))).thenReturn(user);

		UserEntity savedUser = userServiceImpl.createUser(request);

		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getUsername()).isEqualTo("shiv.prasad");
		verify(userRepository).save(any(UserEntity.class));
	}

	@Test
	void testCheckUserByUserId() {
		Long userId = 1L;
		when(userRepository.findById(userId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userServiceImpl.updateUserStatus(userId, false))
				.isInstanceOf(EntityNotFoundException.class).hasMessage(AppConstant.USER_NOT_FOUND + userId);

		verify(userRepository, never()).save(any());
	}

	@Test
	void testUpdateUserStatus() {
		Long userId = 1L;
		user.setActive(true);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));
		when(userContext.getUserId()).thenReturn(200L);
		String result = userServiceImpl.updateUserStatus(userId, false);

		assertThat(result).isNotBlank();
		assertThat(user.isActive()).isFalse();
		assertThat(result).isEqualTo(AppConstant.USER_STATUS);
		verify(userRepository).save(user);

	}

	@Test
	void testUpdateUserStatusAlreadyActive() {
		Long userId = 1L;
		boolean status = false;
		user.setActive(status);
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		String result = userServiceImpl.updateUserStatus(userId, status);

		assertThat(result).isEqualTo("User is already " + (status ? "active" : "inactive"));
		verify(userRepository, never()).save(any());
	}

	@Test
	void testDeleteUser() {
		Long userId = 10L;
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		String result = userServiceImpl.deleteUser(userId);

		assertThat(result).isEqualTo(AppConstant.USER_DELETION);
		verify(userRepository).delete(user);
	}

	@Test
	void testGetAllUsers() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdOn").descending());
		Page<UserEntity> mockPage = new PageImpl<>(List.of(user));

		when(userRepository.findAll(pageable)).thenReturn(mockPage);

		Page<UserEntity> result = userServiceImpl.getAllUsers(0, 10);
		assertThat(result.getSize()).isEqualTo(1);
	}

	@Test
	void testGetUserById() {
		Long userId = 100L;
		when(userRepository.findById(userId)).thenReturn(Optional.of(user));

		UserEntity result = userServiceImpl.getUserById(userId);

		assertThat(result).isNotNull();
		assertThat(result.getUsername()).isEqualTo("shiv.prasad");
	}

//	@Test
//	void testUpdateUserWithNewPassword() {
//		request.setPassword("NewPass@123");
//
//		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//		when(userContext.getUserId()).thenReturn(100L);
//
//		doNothing().when(modelMapper).map(request, user);
//		UserHierarchyEntity hierarchy = new UserHierarchyEntity();
//		when(modelMapper.map(request, UserHierarchyEntity.class)).thenReturn(hierarchy);
//		when(passwordEncoder.encode("NewPass@123")).thenReturn("encodedNewPass");
//		when(modelMapper.map(request, CredentialEntity.class)).thenReturn(new CredentialEntity());
//		when(credentialRepository.findLastThreePassword(eq(1L), any(Pageable.class)))
//				.thenReturn(Collections.emptyList());
//		when(userRepository.save(any(UserEntity.class))).thenReturn(user);
//
//		UserEntity result = userServiceImpl.updateUser(1L, request);
//
//		assertThat(result).isNotNull();
//		verify(modelMapper).map(request, user);
//		verify(userRepository).save(any());
//	}

//	@Test
//	void testUpdateUserWithOldPassword() {
//		request.setPassword("OldPass@123");
//
//		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//		when(userContext.getUserId()).thenReturn(100L);
//		when(credentialRepository.findLastThreePassword(eq(1L), any(Pageable.class)))
//				.thenReturn(List.of(new CredentialEntity() {
//					{
//						setPassword("encodedOldPass");
//					}
//				}));
//
//		when(passwordEncoder.matches("OldPass@123", "encodedOldPass")).thenReturn(true);
//
//		assertThatThrownBy(() -> userServiceImpl.updateUser(1L, request)).isInstanceOf(IllegalArgumentException.class)
//				.hasMessage(AppConstant.OLD_PASSWORD_MATCH);
//
//		verify(userRepository, never()).save(any());
//	}
//
//	@Test
//	void testUpdateUserWithoutPassword() {
//		request.setPassword(null);
//
//		when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//		when(userContext.getUserId()).thenReturn(100L);
//
//		when(modelMapper.map(request, UserHierarchyEntity.class)).thenReturn(new UserHierarchyEntity());
//
//		doNothing().when(modelMapper).map(request, user);
//		when(userRepository.save(any(UserEntity.class))).thenReturn(user);
//
//		UserEntity result = userServiceImpl.updateUser(1L, request);
//
//		assertThat(result).isNotNull();
//		verify(modelMapper).map(request, user);
//		verify(credentialRepository, never()).findLastThreePassword(anyLong(), any());
//	}

}
