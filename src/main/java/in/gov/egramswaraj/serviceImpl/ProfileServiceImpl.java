package in.gov.egramswaraj.serviceimpl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.entity.UserProfileEntity;
import in.gov.egramswaraj.exception.ResourceNotFoundException;
import in.gov.egramswaraj.repo.UserProfileRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.request.UserProfileRequest;
import in.gov.egramswaraj.service.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

	private final UserProfileRepository userProfileRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final UserContext userContext;

	@Override
	public UserProfileEntity createUserProfile(UserProfileRequest request) {
		UserEntity user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.USER_NOT_FOUND + request.getUserId()));

		Optional<UserProfileEntity> existingProfile = userProfileRepository.findByUserId(request.getUserId());
		if (existingProfile.isPresent()) {
			throw new IllegalArgumentException(AppConstant.PROFILE_ALREADY_EXIST + request.getUserId());
		}

		UserProfileEntity profile = modelMapper.map(request, UserProfileEntity.class);
		profile.setCreatedBy(userContext.getUserId());
		profile.setUser(user);
		return userProfileRepository.save(profile);

	}

	@Override
	public UserProfileEntity getUserProfileById(Long id) {
		return userProfileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.PROFILE_NOT_FOUND + id, "profile"));
	}

	@Override
	public UserProfileEntity updateUserProfile(Long id, UserProfileRequest request) {
		UserProfileEntity existingProfile = userProfileRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(AppConstant.PROFILE_NOT_FOUND + id, "profile"));

		UserEntity user = userRepository.findById(request.getUserId()).orElseThrow(
				() -> new ResourceNotFoundException(AppConstant.USER_NOT_FOUND + request.getUserId(), "user"));

		modelMapper.map(request, existingProfile);
		existingProfile.setUser(user);
		existingProfile.setUpdatedBy(userContext.getUserId());
		return userProfileRepository.save(existingProfile);
	}

}
