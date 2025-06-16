package nbcc.springresto.controllers.api;

import nbcc.springresto.dtos.DTOConverters;
import nbcc.springresto.dtos.entity.EventDTOWithMenuDetails;
import nbcc.springresto.services.EventService;
import nbcc.springresto.services.MenuService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nbcc.springresto.dtos.DTOConverters.toEventDTOWithMenuDetails;

@RestController
@RequestMapping("/api/menu")
public class MenuApiController {

    private final MenuService menuService;
    private final EventService eventService;

    public MenuApiController(MenuService menuService, EventService eventService) {
        this.menuService = menuService;
        this.eventService = eventService;
    }

    // get menu details associated to the event
    @GetMapping()
    public List<EventDTOWithMenuDetails> getEventWithMenuDetails() {
        return toEventDTOWithMenuDetails(eventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Long id) {
        var menu = menuService.findById(id);

        if (menu != null) {
            return new ResponseEntity<>(DTOConverters.toMenuDTOWithItems(menu), HttpStatus.OK);
        } else {
            String errorMessage = "Menu with ID " + id + " does not exist.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
}
