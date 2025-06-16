package nbcc.springresto.repositories;

import nbcc.springresto.entities.Event;
import nbcc.springresto.entities.Seating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatingRepository extends JpaRepository<Seating, Long> {
    List<Seating> findByEvent(Event event);
}
