package in.gov.egramswaraj.service;

import java.util.List;

import org.springframework.data.domain.Page;

import in.gov.egramswaraj.entity.ApplicationEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.request.ApplicationRequest;
import jakarta.validation.Valid;

public interface ApplicationService {

	ApplicationEntity createApplication(@Valid ApplicationRequest request);

	String updateApplicationStatus(Long applicationId, boolean flag);
	
	String deleteApplication(Long id);

	Page<ApplicationEntity> getAllApplications(int page, int size);

	ApplicationEntity getApplicationById(Long id);

	ApplicationEntity updateApplication(Long id, @Valid ApplicationRequest request);

	String mapUsersToApplication(Long applicationId, List<Long> userIds , Long groupId);

	Page<UserEntity> getUsersByApplicationId(Long applicationId, int page, int size);

	

}
