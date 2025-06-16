package nbcc.springresto.repositories;

import nbcc.springresto.entities.Layout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LayoutRepository extends JpaRepository<Layout, Long> {
    List<Layout> findActiveLayoutsByIsArchivedFalse();

    List<Layout> findByIsArchivedTrue();
}
