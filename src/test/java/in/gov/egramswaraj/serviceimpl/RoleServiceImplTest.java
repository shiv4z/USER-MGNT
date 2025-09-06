package in.gov.egramswaraj.serviceimpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
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
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.request.RoleRequest;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private ModelMapper modelMapper;
	@Mock
	private UserContext userContext;

	@InjectMocks
	private RoleServiceImpl roleServiceImpl;

	private RoleRequest roleRequest;
	private RoleEntity roleEntity;

	@BeforeEach
	void setUp() {
		roleRequest = new RoleRequest();
		roleRequest.setRoleName("Admin");
		roleRequest.setRoleDescription("Administrator role with all permissions");

		roleEntity = new RoleEntity();
		roleEntity.setId(1L);
		roleEntity.setRoleName("Admin");
		roleEntity.setRoleDescription("Administrator role with all permissions");
		roleEntity.setActive(true);
	}

	@Test
    void testCreateRole() {
        when(roleRepository.existsByRoleName("Admin")).thenReturn(false);
        when(userContext.getUserId()).thenReturn(101L);
        when(modelMapper.map(roleRequest, RoleEntity.class)).thenReturn(roleEntity);
        when(roleRepository.save(roleEntity)).thenReturn(roleEntity);

        RoleEntity savedRole = roleServiceImpl.createRole(roleRequest);

        assertThat(savedRole).isNotNull();
        assertThat(savedRole.getRoleName()).isEqualTo("Admin");
        verify(roleRepository).save(roleEntity);
    }

	@Test
    void testCreateRoleAlreadyExists() {
        when(roleRepository.existsByRoleName("Admin")).thenReturn(true);

        assertThatThrownBy(() -> roleServiceImpl.createRole(roleRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(AppConstant.ROLE_ALREADY_EXIST);

        verify(roleRepository, never()).save(any());
    }

	@Test
	void testUpdateRoleStatusActivate() {
		roleEntity.setActive(false);
		when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
		when(userContext.getUserId()).thenReturn(101L);

		String result = roleServiceImpl.updateRoleStatus(1L, true);

		assertThat(result).isEqualTo(AppConstant.ROLE_STATUS);
		verify(roleRepository).save(roleEntity);
		assertThat(roleEntity.isActive()).isTrue();
	}

	@Test
	    void testUpdateRoleStatusAlreadyActive() {
	        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));

	        String result = roleServiceImpl.updateRoleStatus(1L, true);

	        assertThat(result).isEqualTo("Role is already active");
	        verify(roleRepository, never()).save(any());
	    }

	@Test
	    void testDeleteRole() {
	        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));

	        String result = roleServiceImpl.deleteRole(1L);

	        assertThat(result).isEqualTo(AppConstant.ROLE_DELETION);
	        verify(roleRepository).delete(roleEntity);
	    }

	@Test
	void testGetAllRoles() {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("createdTimestamp").descending());
		Page<RoleEntity> mockPage = new PageImpl<>(List.of(roleEntity));

		when(roleRepository.findAll(pageable)).thenReturn(mockPage);

		Page<RoleEntity> result = roleServiceImpl.getAllRoles(0, 10);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(1);
	}

	@Test
    void testGetRoleById() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));

        RoleEntity result = roleServiceImpl.getRoleById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getRoleName()).isEqualTo("Admin");
    }

	@Test
    void testGetRoleByIdNotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> roleServiceImpl.getRoleById(99L))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage(AppConstant.ROLE_NOT_FOUND + "99");
    }

	@Test
    void testUpdateRole() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(roleEntity));
        when(userContext.getUserId()).thenReturn(101L);

        RoleEntity updated = new RoleEntity();
        updated.setId(1L);
        updated.setRoleName("AdminUpdated");
        updated.setRoleDescription("Updated Description");

        doAnswer(invocation -> {
            RoleRequest source = invocation.getArgument(0);
            RoleEntity dest = invocation.getArgument(1);
            dest.setRoleName(source.getRoleName());
            dest.setRoleDescription(source.getRoleDescription());
            return null;
        }).when(modelMapper).map(roleRequest, roleEntity);

        when(roleRepository.save(roleEntity)).thenReturn(updated);

        roleRequest.setRoleName("AdminUpdated");
        roleRequest.setRoleDescription("Updated Description");

        RoleEntity result = roleServiceImpl.updateRole(1L, roleRequest);

        assertThat(result.getRoleName()).isEqualTo("AdminUpdated");
        assertThat(result.getRoleDescription()).isEqualTo("Updated Description");
    }
}
