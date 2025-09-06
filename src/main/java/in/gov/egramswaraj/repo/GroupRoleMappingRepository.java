package in.gov.egramswaraj.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.GroupRoleMappingEntity;
import in.gov.egramswaraj.entity.RoleEntity;

@Repository
public interface GroupRoleMappingRepository extends JpaRepository<GroupRoleMappingEntity, Long> {
	
	@Query("SELECT gr.role FROM GroupRoleMappingEntity gr WHERE gr.group.id = :groupId")
	Page<RoleEntity> findRolesByGroupId(@Param("groupId") Long groupId, Pageable pageable);
}
