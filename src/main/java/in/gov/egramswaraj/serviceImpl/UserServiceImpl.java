package in.gov.egramswaraj.serviceimpl;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.CredentialEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.entity.UserHierarchyEntity;
import in.gov.egramswaraj.repo.CredentialRepository;
import in.gov.egramswaraj.repo.UserHierarchyRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.request.HierarchyRequest;
import in.gov.egramswaraj.request.PasswordRequest;
import in.gov.egramswaraj.request.UserRequest;
import in.gov.egramswaraj.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final CredentialRepository credentialRepository;
	private final UserHierarchyRepository userHierarchyRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final UserContext userContext;

	@Override
	public UserEntity createUser(UserRequest request) {

		if (userRepository.existsByUsername(request.getUsername())) {
			throw new IllegalArgumentException(AppConstant.USER_ALREADY_EXIST);
		}

		request.setCreatedBy(userContext.getUserId());
		UserEntity user = modelMapper.map(request, UserEntity.class);
		setUserPassword(request, user);
		setUserHierarchy(request, user);
		return userRepository.save(user);
	}

	private boolean hasHierarchy(UserRequest request) {
		return request.getLocalbodyCode() != null;
	}

	@Override
	public String updateUserStatus(Long userId, boolean flag) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.USER_NOT_FOUND + userId));

		if (user.isActive() == flag) {
			return "User is already " + (flag ? "active" : "inactive");
		}

		user.setActive(flag);
		user.setUpdatedBy(userContext.getUserId());
		userRepository.save(user);
		return AppConstant.USER_STATUS;
	}

	@Override
	public String deleteUser(Long userId) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.USER_NOT_FOUND + userId));

		userRepository.delete(user);
		return AppConstant.USER_DELETION;
	}

	@Override
	public Page<UserEntity> getAllUsers(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
		return userRepository.findAll(pageable);
	}

	@Override
	public UserEntity getUserById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.USER_NOT_FOUND + id));
	}


	private void setUserPassword(UserRequest request, UserEntity user) {
		if (isPasswordPresent(request)) {
			CredentialEntity credential = modelMapper.map(request, CredentialEntity.class);
			credential.setPassword(passwordEncoder.encode(request.getPassword()));
			credential.setUser(user);
			credential.setActive(true);
			credential.setCreatedBy(userContext.getUserId());

			Set<CredentialEntity> existingCredentials = user.getCredentials();

			if (existingCredentials == null) {
				existingCredentials = new HashSet<>();
				user.setCredentials(existingCredentials);
			}

			existingCredentials.forEach(cred -> cred.setActive(false));
			existingCredentials.add(credential);
		}
	}

	private void setUserHierarchy(UserRequest request, UserEntity user) {
		if (!hasHierarchy(request))
			return;

		UserHierarchyEntity hierarchy;
		Set<UserHierarchyEntity> hierarchies = new HashSet<>();

		if (user.getHierarchies() != null && !user.getHierarchies().isEmpty()) {
			hierarchy = user.getHierarchies().iterator().next();
			modelMapper.map(request, hierarchy);
		} else {
			hierarchy = modelMapper.map(request, UserHierarchyEntity.class);
		}

		hierarchy.setUser(user);
		hierarchies.add(hierarchy);
		user.setHierarchies(hierarchies);
	}

	private boolean isPasswordPresent(UserRequest request) {
		return request.getPassword() != null && !request.getPassword().isBlank();
	}

	@Override
	public String updatePassword(Long userId, @Valid PasswordRequest request) throws BadRequestException {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.USER_NOT_FOUND + userId));

		checkOldCredential(userId, request);
		validateLastThreePassword(userId, request);
		CredentialEntity newCredential = CredentialEntity.builder().user(user)
				.password(passwordEncoder.encode(request.getNewUserPazz())).isActive(true)
				.createdBy(userContext.getUserId()).build();

		credentialRepository.save(newCredential);
		return AppConstant.PASSWORD_UPDATED;
	}

	private void checkOldCredential(Long userId, PasswordRequest request) throws BadRequestException {
		CredentialEntity activeCredential = credentialRepository.findByUserIdAndIsActiveTrue(userId)
				.orElseThrow(() -> new BadRequestException(AppConstant.CREDENTIAL_NOT_FOUND + userId));

		if (!passwordEncoder.matches(request.getOldUserPazz(), activeCredential.getPassword())) {
			throw new BadRequestException(AppConstant.INCORRECT_OLD_PASSWORD);
		}
	}

	private void validateLastThreePassword(Long userId, PasswordRequest request) throws BadRequestException {
		List<CredentialEntity> lastCredentials = credentialRepository.findLastThreePassword(userId,
				PageRequest.of(0, AppConstant.USER_PASSWORD_VALIDATION));

		for (CredentialEntity credential : lastCredentials) {
			if (passwordEncoder.matches(request.getNewUserPazz(), credential.getPassword())) {
				throw new BadRequestException(AppConstant.OLD_PASSWORD_MATCH);
			}
		}
		deactivateOldCredentials(lastCredentials);

	}

	private void deactivateOldCredentials(List<CredentialEntity> credentials) {
		if (null != credentials && !credentials.isEmpty()) {
			credentials.forEach(cred -> {
				cred.setActive(false);
				cred.setUpdatedBy(userContext.getUserId());
			});
			credentialRepository.saveAll(credentials);
		}
	}

	@Override
	public String updateHierarchy(Long userId, @Valid HierarchyRequest request) {
		UserEntity user = userRepository.findById(userId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.USER_NOT_FOUND + userId));

		deactivateExistingHierarchy(userId);
		UserHierarchyEntity newHierarchy = UserHierarchyEntity.builder().user(user)
				.localbodyCode(request.getLocalbodyCode()).version(request.getVersion()).isActive(true)
				.createdBy(userContext.getUserId()).build();

		userHierarchyRepository.save(newHierarchy);
		return AppConstant.HIERARCHY_UPDATED;
	}

	private void deactivateExistingHierarchy(Long userId) {
		List<UserHierarchyEntity> existingHierarchies = userHierarchyRepository.findByUserIdAndIsActiveTrue(userId);

		if (null != existingHierarchies && !existingHierarchies.isEmpty()) {
			existingHierarchies.stream().peek(h -> {
				h.setActive(false);
				h.setUpdatedBy(userContext.getUserId());
				h.setUpdatedOn(LocalDateTime.now());
			}).collect(Collectors.toList());
			userHierarchyRepository.saveAll(existingHierarchies);
		}
	}

}
