package in.gov.egramswaraj.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.gov.egramswaraj.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
	 boolean existsByUsername(String username);
}
