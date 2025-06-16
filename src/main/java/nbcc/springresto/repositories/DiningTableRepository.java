package nbcc.springresto.repositories;


import nbcc.springresto.entities.DiningTable;
import nbcc.springresto.entities.Layout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DiningTableRepository extends JpaRepository<DiningTable, Long> {

    List<DiningTable> findDiningTablesByLayoutId(Long id);
    List<DiningTable> findByIsArchivedTrue();
    List<DiningTable> findByIsArchivedFalse();

    @Query("""
    SELECT dt FROM DiningTable dt
    WHERE dt.layout.id = :layoutId
    AND dt.isArchived = false
    AND dt.id NOT IN (
        SELECT dt2.id FROM Reservation r JOIN r.diningTable dt2
        WHERE r.seating.seatingDateTime = :seatingDateTime
    )
""")
    List<DiningTable> findAvailableTables(@Param("layoutId") Long layoutId,
                                          @Param("seatingDateTime") LocalDateTime seatingDateTime);

}
