package nbcc.springresto.repositories;

import nbcc.springresto.entities.Menu;
import nbcc.springresto.entities.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByMenu(Menu menu);

    boolean existsByNameAndMenu(String name, Menu menu);
}
