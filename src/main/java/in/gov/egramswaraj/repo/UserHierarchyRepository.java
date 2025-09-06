package in.gov.egramswaraj.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.UserHierarchyEntity;

@Repository
public interface UserHierarchyRepository extends JpaRepository<UserHierarchyEntity, Long> {

	List<UserHierarchyEntity> findByUserIdAndIsActiveTrue(Long userId);

}
