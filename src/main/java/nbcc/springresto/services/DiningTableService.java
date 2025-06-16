package nbcc.springresto.services;


import jakarta.persistence.EntityNotFoundException;
import nbcc.springresto.entities.DiningTable;
import nbcc.springresto.entities.Layout;
import nbcc.springresto.repositories.DiningTableRepository;
import nbcc.springresto.repositories.LayoutRepository;
import nbcc.springresto.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DiningTableService {

    private final DiningTableRepository diningTableRepository;
    private final LayoutRepository layoutRepository;
    private final ReservationRepository reservationRepository;

    public DiningTableService(DiningTableRepository diningTableRepository, LayoutRepository layoutRepository, ReservationRepository reservationRepository) {
        this.diningTableRepository = diningTableRepository;
        this.layoutRepository = layoutRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<DiningTable> findAllActive() {
        return diningTableRepository.findByIsArchivedFalse();
    }

    public DiningTable save(DiningTable diningTable) {
        return diningTableRepository.save(diningTable);
    }


    public void delete(long id) {
        DiningTable diningTable = diningTableRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dining Table not found with ID: " + id));

        boolean isUsedByReservation = reservationRepository.existsByDiningTable(diningTable);

        if (isUsedByReservation) {
            diningTable.setIsArchived(true);
            diningTableRepository.save(diningTable);
        } else {
            diningTableRepository.delete(diningTable);
        }
    }

    public void addDiningTable(Long layoutId, int numberOfSeats) {
        Layout layout = layoutRepository.findById(layoutId).orElse(null);

        if (layout == null) {
            throw new RuntimeException("Layout not found");
        }

        DiningTable diningTable = new DiningTable();
        diningTable.setLayout(layout);
        diningTable.setNumberOfSeats(numberOfSeats);

        diningTableRepository.save(diningTable);
    }


    public void removeDiningTable(long diningTableId) {
        diningTableRepository.deleteById(diningTableId);
    }

    public void removeDiningTableFromLayout(Long tableId) {
        DiningTable diningTable = diningTableRepository.findById(tableId).orElse(null);

        if (diningTable == null) {
            throw new RuntimeException("Dining Table not found");
        }

        diningTableRepository.delete(diningTable);
    }

    public List<DiningTable> findByLayoutId(long layoutId) {

        Layout layout = layoutRepository.findById(layoutId).orElse(null);
        if (layout == null) {
            throw new RuntimeException("Layout not found");
        }

        return diningTableRepository.findDiningTablesByLayoutId(layoutId);
    }

    public List<DiningTable> findAvailableDiningTables(long layoutId, LocalDateTime seatingDateTime ) {
        return diningTableRepository.findAvailableTables(layoutId, seatingDateTime);
    }

    public DiningTable findById(long id) {
        return diningTableRepository.findById(id).orElse(null);
    }

    public List<DiningTable> findAllArchived() {
        return diningTableRepository.findByIsArchivedTrue();
    }

}
