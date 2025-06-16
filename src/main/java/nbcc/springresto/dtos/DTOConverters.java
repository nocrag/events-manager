package nbcc.springresto.dtos;

import nbcc.springresto.dtos.entity.*;
import nbcc.springresto.entities.*;
import nbcc.springresto.services.SeatingService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DTOConverters {
    public static EventDTO toEventDTO(Event event) {
        return new EventDTO(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getPrice(),
                toSeatingDTO(event.getSeatings()),
                toMenuDTO(event.getMenu())
        );
    }

    public static List<EventDTO> toEventDTO(Iterable<Event> events) {
        List<EventDTO> dtos = new ArrayList<>();

        for (var item : events) {
            dtos.add(toEventDTO(item));
        }
        return dtos;
    }

    public static SeatingDTO toSeatingDTO(Seating seating) {
        return new SeatingDTO(
                seating.getId(),
                seating.getSeatingDateTime(),
                seating.getDurationInMinutes()
        );
    }

    public static List<SeatingDTO> toSeatingDTO(Iterable<Seating> seatings) {
        List<SeatingDTO> dtos = new ArrayList<>();

        for (var item : seatings) {
            dtos.add(toSeatingDTO(item));
        }
        return dtos;
    }

    public static MenuDTO toMenuDTO(Menu menu) {
        return new MenuDTO(
                menu.getId(),
                menu.getName(),
                menu.getDescription()
        );
    }

    public static EventDTOWithMenuDetails toEventDTOWithMenuDetails(Event event) {
        return new EventDTOWithMenuDetails(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getStartDate(),
                event.getEndDate(),
                event.getPrice(),
                toMenuDTO(event.getMenu()),
                toMenuItemDTO(event.getMenu().getMenuItems())
        );
    }

    public static List<EventDTOWithMenuDetails> toEventDTOWithMenuDetails(Iterable<Event> events) {
        List<EventDTOWithMenuDetails> dtos = new ArrayList<>();

        for (var item : events) {
            dtos.add(toEventDTOWithMenuDetails(item));
        }
        return dtos;
    }

    public static MenuDTOWithItems toMenuDTOWithItems(Menu menu) {
        return new MenuDTOWithItems(
                menu.getId(),
                menu.getName(),
                menu.getDescription(),
                toMenuItemDTO(menu.getMenuItems()
                ));
    }

    public static MenuItemDTO toMenuItemDTO(MenuItem menuItem) {
        return new MenuItemDTO(
                menuItem.getId(),
                menuItem.getName(),
                menuItem.getDescription()
        );
    }

    public static List<MenuItemDTO> toMenuItemDTO(Iterable<MenuItem> menuItems) {
        List<MenuItemDTO> dtos = new ArrayList<>();

        for (var item : menuItems) {
            dtos.add(toMenuItemDTO(item));
        }
        return dtos;
    }

    public static ReservationDTO toReservationDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getSeating().getEvent().getId(),
                reservation.getSeating().getId(),
                reservation.getFirstName(),
                reservation.getLastName(),
                reservation.getEmail(),
                reservation.getGroupSize());
    }

    public static Reservation toReservation(ReservationDTO dto, SeatingService seatingService) {
        return new Reservation(
                seatingService.findById(dto.getSeatingId()),
                dto.getFirstName(),
                dto.getLastName(),
                dto.getEmail(),
                dto.getGroupSize()
        );
    }


    public static List<ValidationErrorDTO> toValidationErrors(BindingResult bindResult) {
        return toValidationErrors(bindResult.getFieldErrors());
    }

    public static List<ValidationErrorDTO> toValidationErrors(Collection<FieldError> errors) {

        return errors.stream().map(fieldError ->
                new ValidationErrorDTO(fieldError.getField(), fieldError.getDefaultMessage())).toList();
    }
}
