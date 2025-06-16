package nbcc.springresto.services;

import jakarta.persistence.EntityNotFoundException;
import nbcc.springresto.entities.Event;
import nbcc.springresto.entities.Seating;
import nbcc.springresto.repositories.SeatingRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SeatingService {
    private final SeatingRepository seatingRepository;

    public SeatingService(SeatingRepository seatingRepository) {
        this.seatingRepository = seatingRepository;
    }

    public Seating create(Seating seating) {
        return seatingRepository.save(seating);
    }

    public Seating update(Long id, Seating seating) {
        var dbOptionalSeating = seatingRepository.findById(id);

        if (dbOptionalSeating.isEmpty()) {
            throw new EntityNotFoundException("Seating not found with id " + id);
        }
        var dbEvent = dbOptionalSeating.get();

        dbEvent.setSeatingDateTime(seating.getSeatingDateTime());
        dbEvent.setDurationInMinutes(seating.getDurationInMinutes());

        return seatingRepository.save(dbEvent);
    }

    public Seating findById(Long id) {
        return seatingRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        seatingRepository.deleteById(id);
    }

    public List<Seating> findSeatingsByEvent(Event event) {
        return seatingRepository.findByEvent(event);
    }

    public List<Seating> findSeatingsByEventAndDate(Event event, LocalDate date) {
        List<Seating> seatings = seatingRepository.findByEvent(event);

        List<Seating> result = new ArrayList<>();

        for (Seating seating : seatings) {
            if (seating.getSeatingDateTime().toLocalDate().equals(date)) {
                result.add(seating);
            }
        }

        return result;
    }


    public List<LocalDate> getEventSeatingDates(Event event) {
        List<Seating> seatings = seatingRepository.findByEvent(event);
        Set<LocalDate> seatingDates = new HashSet<>();

        for (Seating seating : seatings) {
            seatingDates.add(seating.getSeatingDateTime().toLocalDate());
        }

        return new ArrayList<>(seatingDates);
    }

    public void validateSeatingDates(Event event, Seating seating) {
        // checking if seating is within the event's date range...
        if (seating.getSeatingDateTime().toLocalDate().isBefore(event.getStartDate()) ||
                seating.getSeatingDateTime().toLocalDate().isAfter(event.getEndDate())) {
            throw new IllegalArgumentException("Seating date and time must be within the event's date range.");
        }

        // checking if seating's duration exceeds event's end date...
        LocalDateTime seatingDuration = seating.getSeatingDateTime().plusMinutes(seating.getDurationInMinutes());
        if (seatingDuration.toLocalDate().isAfter(event.getEndDate())) {
            throw new IllegalArgumentException("Seating duration cannot exceed the event's end date.");
        }
    }

    public List<Seating> findAll() {
        return seatingRepository.findAll();
    }
}