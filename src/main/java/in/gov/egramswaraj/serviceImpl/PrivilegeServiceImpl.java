package in.gov.egramswaraj.serviceimpl;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import in.gov.egramswaraj.constant.AppConstant;
import in.gov.egramswaraj.constant.UserContext;
import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.repo.PrivilegeRepository;
import in.gov.egramswaraj.request.PrivilegeRequest;
import in.gov.egramswaraj.service.PrivilegeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {

	private final PrivilegeRepository privilegeRepository;
	private final ModelMapper modelMapper;
	private final UserContext userContext;

	@Override
	public PrivilegeEntity createPrivilege(PrivilegeRequest request) {
		if (privilegeRepository.existsByPrivilegeName(request.getPrivilegeName() )) {
			throw new IllegalArgumentException(AppConstant.PRIVILEGE_NAME_ALREADY_EXIST);
		}
		
		if (privilegeRepository.existsByPrivilegeUrl(request.getPrivilegeUrl() )) {
			throw new IllegalArgumentException(AppConstant.PRIVILEGE_URL_ALREADY_EXIST);
		}
		request.setCreatedBy(userContext.getUserId());
		PrivilegeEntity Privilege = modelMapper.map(request, PrivilegeEntity.class);
		return privilegeRepository.save(Privilege);
	}

	@Override
	public String updatePrivilegeStatus(Long PrivilegeId, boolean flag) {
		PrivilegeEntity Privilege = privilegeRepository.findById(PrivilegeId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.PRIVILEGE_NOT_FOUND + PrivilegeId));

		if (Privilege.isActive() == flag) {
			return "Privilege is already " + (flag ? "active" : "inactive");
		}
		Privilege.setActive(flag);
		Privilege.setUpdatedBy(userContext.getUserId());
		privilegeRepository.save(Privilege);

		return AppConstant.PRIVILEGE_STATUS;
	}
	
	@Override
	public String deletePrivilege(Long PrivilegeId) {
		PrivilegeEntity Privilege = privilegeRepository.findById(PrivilegeId)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.PRIVILEGE_NOT_FOUND + PrivilegeId));

		privilegeRepository.delete(Privilege);
		return AppConstant.PRIVILEGE_DELETION;
	}

	@Override
	public Page<PrivilegeEntity> getAllPrivileges(int page, int size) {
		Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
		return privilegeRepository.findAll(pageable);
	}

	@Override
	public PrivilegeEntity getPrivilegeById(Long id) {
		return privilegeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.PRIVILEGE_NOT_FOUND + id));
	}

	@Override
	public PrivilegeEntity updatePrivilege(Long id, @Valid PrivilegeRequest request) {
		PrivilegeEntity Privilege = privilegeRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException(AppConstant.PRIVILEGE_NOT_FOUND + id));
		request.setUpdatedBy(userContext.getUserId());
		modelMapper.map(request, Privilege);
		return privilegeRepository.save(Privilege);
	}

}
