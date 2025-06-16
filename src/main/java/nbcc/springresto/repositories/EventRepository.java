package nbcc.springresto.repositories;

import nbcc.springresto.entities.Event;
import nbcc.springresto.entities.Layout;
import nbcc.springresto.entities.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByStartDateAfterAndIsArchivedFalse(LocalDate startDate);

    List<Event> findAllByStartDateBeforeAndIsArchivedFalse(LocalDate startDate);

    List<Event> findAllByStartDateBetweenAndIsArchivedFalse(LocalDate startDate1, LocalDate startDate2);

    List<Event> findAllByIsArchivedFalse();

    List<Event> findAllByMenu(Menu menu);

    boolean existsByLayout(Layout layout);

}
