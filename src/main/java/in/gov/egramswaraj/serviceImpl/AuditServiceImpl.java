package in.gov.egramswaraj.serviceimpl;

import org.springframework.stereotype.Service;

import in.gov.egramswaraj.entity.AuditLog;
import in.gov.egramswaraj.repo.AuditLogRepository;
import in.gov.egramswaraj.service.AuditService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

	private final AuditLogRepository auditLogRepository;

	public void saveAuditLog(AuditLog log) {
        auditLogRepository.save(log);
    }

}
