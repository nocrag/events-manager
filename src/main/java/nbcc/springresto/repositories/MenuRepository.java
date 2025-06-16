package nbcc.springresto.repositories;

import nbcc.springresto.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MenuRepository extends JpaRepository<Menu, Long> {
    boolean existsByName(String name);
}
