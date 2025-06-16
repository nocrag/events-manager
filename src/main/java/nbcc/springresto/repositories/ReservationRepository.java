package nbcc.springresto.repositories;


import nbcc.springresto.entities.*;
import nbcc.springresto.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findBySeating(Seating seating);

    // List<Reservation> findByStatus(ReservationStatus status);
    List<Reservation> findAllBySeating_Event(Event event);

    //List<Reservation> findBySeating_Event_Id(Long eventId);
    //List<Reservation> findBySeating_Event_IdAndStatus(Long eventId, ReservationStatus status);
    List<Reservation> findAllByOrderBySeating_SeatingDateTimeAsc();

    List<Reservation> findByStatusOrderBySeating_SeatingDateTimeAsc(ReservationStatus status);

    List<Reservation> findBySeating_Event_IdOrderBySeating_SeatingDateTimeAsc(Long eventId);

    List<Reservation> findBySeating_Event_IdAndStatusOrderBySeating_SeatingDateTimeAsc(Long eventId, ReservationStatus status);

    boolean existsByDiningTable(DiningTable diningTable);


}
