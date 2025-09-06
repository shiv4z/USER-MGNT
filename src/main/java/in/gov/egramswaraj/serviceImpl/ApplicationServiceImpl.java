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
import in.gov.egramswaraj.entity.ApplicationEntity;
import in.gov.egramswaraj.entity.ApplicationMappingEntity;
import in.gov.egramswaraj.entity.GroupEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.exception.SomeUsersNotFoundException;
import in.gov.egramswaraj.repo.ApplicationMappingRepository;
import in.gov.egramswaraj.repo.ApplicationRepository;
import in.gov.egramswaraj.repo.GroupRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.request.ApplicationRequest;
import in.gov.egramswaraj.service.ApplicationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

	private final ApplicationRepository applicationRepository;
	private final ApplicationMappingRepository applicationMappingRepository;
	private final GroupRepository groupRepository;
	private final UserRepository userRepository;
	private final ModelMapper modelMapper;
	private final UserContext userContext;

	@Override
	public ApplicationEntity createApplication(ApplicationRequest request) {
		if (applicationRepository.existsByApplicationName(request.getApplicationName())) {
			throw new IllegalArgumentException(AppConstant.APPLICATION_NAME_ALREADY_EXIST);
		}
		if (applicationRepository.existsByApplicationUrl(request.getApplicationUrl())) {
			throw new IllegalArgumentException(AppConstant.APPLICATION_URL_ALREADY_EXIST);
		}
		request.setCreatedBy(userContext.getUserId());
		ApplicationEntity application = modelMapper.map(request, ApplicationEntity.class);
		return applicationRepository.save(application);
	}

	@Override
	public String updateApplicationStatus(Long applicationId, boolean flag) {
		ApplicationEntity application = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.APPLICATION_NOT_FOUND + applicationId));

		if (application.isActive() == flag) {
			return "Application is already " + (flag ? "active" : "inactive");
		}

		application.setActive(false);
		application.setUpdatedBy(userContext.getUserId());
		applicationRepository.save(application);
		return AppConstant.APPLICATION_STATUS;
	}
	
	@Override
	public String deleteApplication(Long applicationId) {
		ApplicationEntity resource = applicationRepository.findById(applicationId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.APPLICATION_NOT_FOUND + applicationId));

		applicationRepository.delete(resource);
		return AppConstant.APPLICATION_DELETION;
	}

	@Override
	public Page<ApplicationEntity> getAllApplications(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
		return applicationRepository.findAll(pageable);
	}

	@Override
	public ApplicationEntity getApplicationById(Long id) {
		return applicationRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.APPLICATION_NOT_FOUND + id));

	}

	@Override
	public ApplicationEntity updateApplication(Long id, @Valid ApplicationRequest request) {
		ApplicationEntity application = applicationRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.APPLICATION_NOT_FOUND + id));
      
		request.setUpdatedBy(userContext.getUserId());
		modelMapper.map(request, application);
		return applicationRepository.save(application);
	}

	@Override
	public String mapUsersToApplication(Long applicationId, List<Long> userIds, Long groupId) {

		ApplicationEntity application = applicationRepository.findById(applicationId).orElseThrow(() -> 
			 new RuntimeException(AppConstant.APPLICATION_NOT_FOUND + applicationId)
		);

		List<UserEntity> users = userRepository.findAllById(userIds);

		if (users.size() != userIds.size()) {
			throw new SomeUsersNotFoundException(AppConstant.SOME_USERS_NOT_FOUND);
		}
		
		GroupEntity group = groupRepository.findById(groupId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.GROUP_NOT_FOUND + groupId));
		

		List<ApplicationMappingEntity> mappings = users.stream().map(user -> {
			ApplicationMappingEntity mapping = new ApplicationMappingEntity();
			mapping.setUser(user);
			mapping.setApplication(application);
			mapping.setGroup(group);
			return mapping;
		}).toList();

		applicationMappingRepository.saveAll(mappings);
		return AppConstant.APPLICATION_MAPPING_STATUS;
	}

	@Override
	public Page<UserEntity> getUsersByApplicationId(Long applicationId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return applicationMappingRepository.findUsersByApplicationId(applicationId, pageRequest);
    }

}
