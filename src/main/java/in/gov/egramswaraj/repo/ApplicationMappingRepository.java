package in.gov.egramswaraj.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.ApplicationMappingEntity;
import in.gov.egramswaraj.entity.UserEntity;

@Repository
public interface ApplicationMappingRepository extends JpaRepository<ApplicationMappingEntity, Long> {
	
	List<ApplicationMappingEntity> findByUserId(Long userId);

	List<ApplicationMappingEntity> findByApplicationId(Long applicationId);

	@Query("SELECT am.user FROM ApplicationMappingEntity am WHERE am.application.id = :applicationId")
	Page<UserEntity> findUsersByApplicationId(@Param("applicationId")Long applicationId, Pageable pageable);
}
