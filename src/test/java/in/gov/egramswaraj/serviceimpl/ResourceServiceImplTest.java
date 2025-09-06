package in.gov.egramswaraj.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.repo.PrivilegeRepository;
import in.gov.egramswaraj.request.PrivilegeRequest;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class PrivilegeServiceImplTest {
	@Mock
	private PrivilegeRepository privilegeRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UserContext userContext;

	@InjectMocks
	private PrivilegeServiceImpl resourceServiceImpl;

	private PrivilegeRequest request;
	private PrivilegeEntity resource;

	@BeforeEach
	void setUp() {
		request = new PrivilegeRequest();
		request.setPrivilegeName("Dashboard");
		request.setPrivilegeUrl("http://example.com/dashboard");
		request.setDescription("Dashboard page");

		resource = new PrivilegeEntity();
		resource.setId(1L);
		resource.setPrivilegeName("Dashboard");
		resource.setPrivilegeUrl("http://example.com/dashboard");
		resource.setDescription("Dashboard page");
		resource.setActive(true);
	}

	@Test
    void testCreatePrivilege_success() {
        when(privilegeRepository.existsByPrivilegeName("Dashboard")).thenReturn(false);
        when(privilegeRepository.existsByPrivilegeUrl("http://example.com/dashboard")).thenReturn(false);
        when(userContext.getUserId()).thenReturn(1L);
        when(modelMapper.map(request, PrivilegeEntity.class)).thenReturn(resource);
        when(privilegeRepository.save(resource)).thenReturn(resource);

        PrivilegeEntity saved = resourceServiceImpl.createPrivilege(request);

        assertThat(saved).isNotNull();
        assertThat(saved.getPrivilegeName()).isEqualTo("Dashboard");
        verify(privilegeRepository).save(resource);
    }

	@Test
    void testCreatePrivilege_duplicateName() {
        when(privilegeRepository.existsByPrivilegeName("Dashboard")).thenReturn(true);

        assertThatThrownBy(() -> resourceServiceImpl.createPrivilege(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(AppConstant.PRIVILEGE_NAME_ALREADY_EXIST);
    }

	@Test
    void testCreatePrivilege_duplicateUrl() {
        when(privilegeRepository.existsByPrivilegeName("Dashboard")).thenReturn(false);
        when(privilegeRepository.existsByPrivilegeUrl("http://example.com/dashboard")).thenReturn(true);

        assertThatThrownBy(() -> resourceServiceImpl.createPrivilege(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(AppConstant.PRIVILEGE_URL_ALREADY_EXIST);
    }

	@Test
    void testUpdatePrivilegeStatus_success() {
        when(privilegeRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(userContext.getUserId()).thenReturn(2L);

        String result = resourceServiceImpl.updatePrivilegeStatus(1L, false);

        assertThat(result).isEqualTo(AppConstant.PRIVILEGE_STATUS);
        assertThat(resource.isActive()).isFalse();
        verify(privilegeRepository).save(resource);
    }

	@Test
	void testUpdatePrivilegeStatus_alreadySame() {
		resource.setActive(true);
		when(privilegeRepository.findById(1L)).thenReturn(Optional.of(resource));

		String result = resourceServiceImpl.updatePrivilegeStatus(1L, true);

		assertThat(result).isEqualTo("Privilege is already active");
		verify(privilegeRepository, never()).save(any());
	}

	@Test
    void testDeletePrivilege_success() {
        when(privilegeRepository.findById(1L)).thenReturn(Optional.of(resource));

        String result = resourceServiceImpl.deletePrivilege(1L);

        assertThat(result).isEqualTo(AppConstant.PRIVILEGE_DELETION);
        verify(privilegeRepository).delete(resource);
    }

	@Test
	void testGetAllPrivileges() {
		Page<PrivilegeEntity> page = new PageImpl<>(List.of(resource));
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdOn").descending());
		when(privilegeRepository.findAll(pageable)).thenReturn(page);

		Page<PrivilegeEntity> result = resourceServiceImpl.getAllPrivileges(0, 10);

		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent().get(0).getPrivilegeName()).isEqualTo("Dashboard");
	}

	@Test
    void testGetUserById_found() {
        when(privilegeRepository.findById(1L)).thenReturn(Optional.of(resource));

        PrivilegeEntity result = resourceServiceImpl.getPrivilegeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getPrivilegeName()).isEqualTo("Dashboard");
    }

	@Test
    void testGetUserById_notFound() {
        when(privilegeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> resourceServiceImpl.getPrivilegeById(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage(AppConstant.PRIVILEGE_NOT_FOUND + "99");
    }

	@Test
    void testUpdatePrivilege_success() {
        when(privilegeRepository.findById(1L)).thenReturn(Optional.of(resource));
        when(userContext.getUserId()).thenReturn(100L);
        when(privilegeRepository.save(any())).thenReturn(resource);

        PrivilegeEntity updated = resourceServiceImpl.updatePrivilege(1L, request);

        assertThat(updated).isNotNull();
        verify(modelMapper).map(request, resource);
        verify(privilegeRepository).save(resource);
    }
}
