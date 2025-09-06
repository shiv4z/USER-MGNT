package in.gov.egramswaraj.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.ApplicationEntity;
import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.exception.SomeUsersNotFoundException;
import in.gov.egramswaraj.repo.ApplicationMappingRepository;
import in.gov.egramswaraj.repo.ApplicationRepository;
import in.gov.egramswaraj.repo.UserRepository;
import in.gov.egramswaraj.request.ApplicationRequest;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {
	@Mock
	private ApplicationRepository applicationRepository;
	@Mock
	private ApplicationMappingRepository applicationMappingRepository;
	@Mock
	private UserRepository userRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UserContext userContext;

	@InjectMocks
	private ApplicationServiceImpl applicationServiceImpl;

	private ApplicationRequest request;
	private ApplicationEntity application;

	@BeforeEach
	void setUp() {
		request = new ApplicationRequest();
		request.setApplicationName("Egram");
		request.setApplicationUrl("http://egram.gov.in");

		application = new ApplicationEntity();
		application.setId(1L);
		application.setApplicationName("Egram");
		application.setApplicationUrl("http://egram.gov.in");
		application.setActive(true);
	}

	@Test
    void testCreateApplication() {
        when(applicationRepository.existsByApplicationName("Egram")).thenReturn(false);
        when(applicationRepository.existsByApplicationUrl("http://egram.gov.in")).thenReturn(false);
        when(userContext.getUserId()).thenReturn(1L);
        when(modelMapper.map(request, ApplicationEntity.class)).thenReturn(application);
        when(applicationRepository.save(application)).thenReturn(application);

        ApplicationEntity result = applicationServiceImpl.createApplication(request);

        assertThat(result).isNotNull();
        assertThat(result.getApplicationName()).isEqualTo("Egram");
        verify(applicationRepository).save(application);
    }

	@Test
    void testCreateApplicationDuplicateName() {
        when(applicationRepository.existsByApplicationName("Egram")).thenReturn(true);

        assertThatThrownBy(() -> applicationServiceImpl.createApplication(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(AppConstant.APPLICATION_NAME_ALREADY_EXIST);

        verify(applicationRepository, never()).save(any());
    }

	@Test
    void testCreateApplicationDuplicateUrl() {
        when(applicationRepository.existsByApplicationName("Egram")).thenReturn(false);
        when(applicationRepository.existsByApplicationUrl("http://egram.gov.in")).thenReturn(true);

        assertThatThrownBy(() -> applicationServiceImpl.createApplication(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(AppConstant.APPLICATION_URL_ALREADY_EXIST);
    }

	@Test
	void testUpdateApplicationStatus() {
		application.setActive(true);
		when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
		when(userContext.getUserId()).thenReturn(2L);

		String result = applicationServiceImpl.updateApplicationStatus(1L, false);

		assertThat(result).isEqualTo(AppConstant.APPLICATION_STATUS);
		assertThat(application.isActive()).isFalse();
		verify(applicationRepository).save(application);
	}

	@Test
	void testUpdateApplicationStatusAlreadyInactive() {
		application.setActive(false);
		when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));

		String result = applicationServiceImpl.updateApplicationStatus(1L, false);

		assertThat(result).isEqualTo("Application is already inactive");
		verify(applicationRepository, never()).save(any());
	}

	@Test
    void testDeleteApplication() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));

        String result = applicationServiceImpl.deleteApplication(1L);

        assertThat(result).isEqualTo(AppConstant.APPLICATION_DELETION);
        verify(applicationRepository).delete(application);
    }

	@Test
    void testGetApplicationById() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));

        ApplicationEntity result = applicationServiceImpl.getApplicationById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getApplicationName()).isEqualTo("Egram");
    }

	@Test
    void testGetApplicationByIdNotFound() {
        when(applicationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationServiceImpl.getApplicationById(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage(AppConstant.APPLICATION_NOT_FOUND + "99");
    }

	@Test
    void testUpdateApplication() {
        when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
        when(userContext.getUserId()).thenReturn(100L);
        when(applicationRepository.save(any())).thenReturn(application);

        ApplicationEntity result = applicationServiceImpl.updateApplication(1L, request);

        assertThat(result).isNotNull();
        verify(modelMapper).map(request, application);
        verify(applicationRepository).save(application);
    }

	@Test
	void testMapUsersToApplication() {
		UserEntity user1 = new UserEntity();
		user1.setId(10L);
		UserEntity user2 = new UserEntity();
		user2.setId(11L);
		List<UserEntity> users = List.of(user1, user2);
		List<Long> userIds = List.of(10L, 11L);

		when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
		when(userRepository.findAllById(userIds)).thenReturn(users);

		String result = applicationServiceImpl.mapUsersToApplication(1L, userIds, 1L);

		assertThat(result).isEqualTo(AppConstant.APPLICATION_MAPPING_STATUS);
		verify(applicationMappingRepository).saveAll(anyList());
	}

	@Test
	void testMapUsersToApplicationSomeUsersNotFound() {
		List<Long> userIds = List.of(10L, 11L);
		when(applicationRepository.findById(1L)).thenReturn(Optional.of(application));
		when(userRepository.findAllById(userIds)).thenReturn(List.of(new UserEntity())); // Only 1 returned

		assertThatThrownBy(() -> applicationServiceImpl.mapUsersToApplication(1L, userIds, 1L))
				.isInstanceOf(SomeUsersNotFoundException.class).hasMessage(AppConstant.SOME_USERS_NOT_FOUND);
	}

	@Test
	void testGetAllApplications() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdTimestamp").descending());
		Page<ApplicationEntity> page = new PageImpl<>(List.of(application));

		when(applicationRepository.findAll(pageable)).thenReturn(page);

		Page<ApplicationEntity> result = applicationServiceImpl.getAllApplications(0, 10);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getApplicationName()).isEqualTo("Egram");
	}
}
