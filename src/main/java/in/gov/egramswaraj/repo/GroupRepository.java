package in.gov.egramswaraj.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.GroupEntity;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long>{

	boolean existsByGroupName(String groupName);

}
