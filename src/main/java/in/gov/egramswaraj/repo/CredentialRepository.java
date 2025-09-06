package in.gov.egramswaraj.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.gov.egramswaraj.entity.CredentialEntity;

public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {
	Optional<CredentialEntity> findByUserId(Long userId);

	@Query("SELECT c FROM CredentialEntity c " + "WHERE c.user.id = :userId " + "ORDER BY c.createdOn DESC")
	List<CredentialEntity> findLastThreePassword(@Param("userId") Long userId, Pageable pageable);

	Optional<CredentialEntity> findByUserIdAndIsActiveTrue(Long userId);

}
