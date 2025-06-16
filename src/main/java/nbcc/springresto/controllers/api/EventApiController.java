package nbcc.springresto.controllers.api;

import nbcc.springresto.dtos.entity.EventDTO;
import nbcc.springresto.services.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nbcc.springresto.dtos.DTOConverters.toEventDTO;

@RestController
@RequestMapping("/api/event")
public class EventApiController {

    private final EventService eventService;

    public EventApiController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDTO> getAll() {
        return toEventDTO(eventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        var event = eventService.findById(id);

        if (event != null) {
            return new ResponseEntity<>(toEventDTO(event), HttpStatus.OK);
        } else {
            String errorMessage = "Event with ID " + id + " does not exist.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
}
