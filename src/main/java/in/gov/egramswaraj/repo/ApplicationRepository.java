package in.gov.egramswaraj.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.ApplicationEntity;


@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, Long>{

	boolean existsByApplicationName(String applicationName);

	boolean existsByApplicationUrl(String applicationUrl);

}
