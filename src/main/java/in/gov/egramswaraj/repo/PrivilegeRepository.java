package in.gov.egramswaraj.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.PrivilegeEntity;

@Repository
public interface PrivilegeRepository extends JpaRepository<PrivilegeEntity, Long> {

	boolean existsByPrivilegeName(String resourceName);

	boolean existsByPrivilegeUrl(String resourceUrl);

}
