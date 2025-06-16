package nbcc.springresto.repositories.user;

import nbcc.springresto.entities.user.UserLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserLoginRepository extends JpaRepository<UserLogin, String> {
}
