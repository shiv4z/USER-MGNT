package in.gov.egramswaraj.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.PrivilegeEntity;
import in.gov.egramswaraj.entity.PrivilegeMappingEntity;

@Repository
public interface PrivilegeMappingRepository extends JpaRepository<PrivilegeMappingEntity, Long> {
	
    @Query("SELECT pm.privilege FROM PrivilegeMappingEntity pm WHERE pm.role.id = :roleId")
    Page<PrivilegeEntity> findPrivilegesByRoleId(@Param("roleId") Long roleId, Pageable pageable);

}
