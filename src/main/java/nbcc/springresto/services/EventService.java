package nbcc.springresto.services;

import jakarta.persistence.EntityNotFoundException;
import nbcc.springresto.entities.Event;
import nbcc.springresto.entities.Menu;
import nbcc.springresto.repositories.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final SeatingService seatingService;

    public EventService(EventRepository eventRepository, SeatingService seatingService) {
        this.eventRepository = eventRepository;
        this.seatingService = seatingService;
    }

    public List<Event> findAll() {
        return eventRepository.findAllByIsArchivedFalse();
    }

    @Transactional
    public Event create(Event event) {
        return eventRepository.save(event);
    }

    public Event update(Long id, Event event) {
        var dbOptionalEvent = eventRepository.findById(id);

        if (dbOptionalEvent.isEmpty()) {
            throw new EntityNotFoundException("Event not found with id " + id);
        }
        var dbEvent = dbOptionalEvent.get();

        dbEvent.setName(event.getName());
        dbEvent.setDescription(event.getDescription());
        dbEvent.setStartDate(event.getStartDate());
        dbEvent.setEndDate(event.getEndDate());
        dbEvent.setPrice(event.getPrice());
        dbEvent.setArchived(event.isArchived());
        dbEvent.setMenu(event.getMenu());
        dbEvent.setLayout(event.getLayout());

        return eventRepository.save(dbEvent);
    }

    public Event findById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    public List<Event> findAllByMenu(Menu menu) {
        return eventRepository.findAllByMenu(menu);
    }

    public List<Event> findEventsAfterStartDate(LocalDate startDate) {
        return eventRepository.findAllByStartDateAfterAndIsArchivedFalse(startDate);
    }

    public List<Event> findEventsBeforeStartDate(LocalDate startDate) {
        return eventRepository.findAllByStartDateBeforeAndIsArchivedFalse(startDate);
    }

    public List<Event> findEventsBetweenDates(LocalDate startDate1, LocalDate startDate2) {
        return eventRepository.findAllByStartDateBetweenAndIsArchivedFalse(startDate1, startDate2);
    }

    public void validateEventDates(Event event) {
        if (event.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Start date cannot be in the past.");
        }

        if (event.getStartDate().isAfter(event.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        if (event.getEndDate().isBefore(event.getStartDate())) {
            throw new IllegalArgumentException("End date cannot be before start date.");
        }
    }

    public void validateEventSeatingDate(Event event) {
        List<LocalDate> seatingDates = seatingService.getEventSeatingDates(event);
        for (LocalDate seatingDate : seatingDates) {
            if (seatingDate.isBefore(event.getStartDate()) ||
                    seatingDate.isAfter(event.getEndDate())) {
                throw new IllegalArgumentException("Error: Event's seating date(s) outside of the " +
                        "provided date range.");
            }
        }
    }

    public boolean isSameDayEvent(Event event) {
        return event.getStartDate().isEqual(event.getEndDate());
    }
}
