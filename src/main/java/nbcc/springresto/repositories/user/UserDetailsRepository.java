package nbcc.springresto.repositories.user;

import nbcc.springresto.entities.user.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    UserDetails getUserDetailsByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
