package AccomManage.system.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import AccomManage.system.Entity.User;
 
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	  Optional<User> findByEmail(String email);
	    boolean existsByEmail(String email);
}

