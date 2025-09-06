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

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.entity.RoleEntity;
import in.gov.egramswaraj.repo.PrivilegeMappingRepository;
import in.gov.egramswaraj.repo.PrivilegeRepository;
import in.gov.egramswaraj.repo.RoleRepository;
import in.gov.egramswaraj.serviceimpl.PrivilegeMappingServiceImpl;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class ResourceMappingServiceImplTest {
	@Mock
	private RoleRepository roleRepository;
	@Mock
	private PrivilegeRepository privilegeRepository;
	@Mock
	private PrivilegeMappingRepository PrivilegeMappingRepository;

	@InjectMocks
	private PrivilegeMappingServiceImpl privilegeMappingServiceImpl;

	private RoleEntity role;
	private PrivilegeEntity resource1;
	private PrivilegeEntity resource2;

	@BeforeEach
	void setUp() {
		role = new RoleEntity();
		role.setId(1L);
		role.setRoleName("ADMIN");

		resource1 = new PrivilegeEntity();
		resource1.setId(101L);
		resource1.setPrivilegeName("Dashboard");

		resource2 = new PrivilegeEntity();
		resource2.setId(102L);
		resource2.setPrivilegeName("Settings");
	}

	@Test
	void testMapResourcesToRole() {
		List<Long> resourceIds = List.of(101L, 102L);
		List<PrivilegeEntity> resources = List.of(resource1, resource2);

		when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
		when(privilegeRepository.findAllById(resourceIds)).thenReturn(resources);

		String result = privilegeMappingServiceImpl.mapPrivilegesToRole(1L, resourceIds);

		assertThat(result).isEqualTo(AppConstant.PRIVILEGE_ROLE_MAPPING);
		verify(PrivilegeMappingRepository).saveAll(anyList());
	}

	@Test
	void testMapResourcesToRoleRoleNotFound() {
		Long roleId = 1L;
		List<Long> userIds = List.of(101L);
		when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> privilegeMappingServiceImpl.mapPrivilegesToRole(roleId, userIds))
				.isInstanceOf(EntityNotFoundException.class).hasMessageContaining(AppConstant.ROLE_NOT_FOUND + roleId);

		verify(privilegeRepository, never()).findAllById(any());
		verify(PrivilegeMappingRepository, never()).saveAll(any());
	}

	@Test
	void testMapResourcesToRoleSomeResourcesNotFound() {
		List<Long> resourceIds = List.of(101L, 102L);
		List<PrivilegeEntity> partialResources = List.of(resource1);

		when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
		when(privilegeRepository.findAllById(resourceIds)).thenReturn(partialResources);

		assertThatThrownBy(() -> privilegeMappingServiceImpl.mapPrivilegesToRole(1L, resourceIds))
				.isInstanceOf(EntityNotFoundException.class).hasMessageContaining(AppConstant.INVALID_PRIVILEGE);

		verify(PrivilegeMappingRepository, never()).saveAll(any());
	}

}
