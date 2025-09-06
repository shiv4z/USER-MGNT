package in.gov.egramswaraj.service;

import in.gov.egramswaraj.entity.AuditLog;

public interface AuditService {

	void saveAuditLog(AuditLog log);

}
