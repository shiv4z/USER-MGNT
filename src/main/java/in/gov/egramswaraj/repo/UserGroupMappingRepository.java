package in.gov.egramswaraj.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.UserEntity;
import in.gov.egramswaraj.entity.UserGroupMappingEntity;

@Repository
public interface UserGroupMappingRepository extends JpaRepository<UserGroupMappingEntity, Long> {

    @Query("SELECT ug.user FROM UserGroupMappingEntity ug WHERE ug.group.id = :groupId")
    Page<UserEntity> findUsersByGroupId(@Param("groupId") Long groupId, Pageable pageable);
}
