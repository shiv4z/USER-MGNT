package in.gov.egramswaraj.service;

import in.gov.egramswaraj.entity.UserProfileEntity;
import in.gov.egramswaraj.request.UserProfileRequest;

public interface ProfileService {

	UserProfileEntity createUserProfile(UserProfileRequest request);

	UserProfileEntity getUserProfileById(Long id);

	UserProfileEntity updateUserProfile(Long id, UserProfileRequest request);

}
