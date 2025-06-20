package nbcc.springresto.controllers;


import jakarta.validation.Valid;
import nbcc.springresto.entities.*;
import nbcc.springresto.enums.ReservationStatus;
import nbcc.springresto.services.*;
import nbcc.springresto.services.user.LoginServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ReservationController {

    private final ReservationService reservationService;
    private final SeatingService seatingService;
    private final LoginServiceImpl loginService;
    private final EventService eventService;
    private final DiningTableService diningTableService;
    private final LayoutService layoutService;


    public ReservationController(ReservationService reservationService, SeatingService seatingService, LoginServiceImpl loginService, EventService eventService, DiningTableService diningTableService, LayoutService layoutService) {
        this.reservationService = reservationService;
        this.seatingService = seatingService;
        this.loginService = loginService;
        this.eventService = eventService;
        this.diningTableService = diningTableService;
        this.layoutService = layoutService;
    }

    @ModelAttribute
    public void addLoggedInAttribute(Model model) {
        model.addAttribute("isLoggedIn", loginService.isCurrentLoginValid());
    }

    @GetMapping("/reservations")
    public String get(@RequestParam(value = "eventId", required = false) Long eventId,
                      @RequestParam(value = "status", required = false) ReservationStatus status,
                      Model model) {

        List<Event> allEvents = eventService.findAll();
        List<Reservation> reservations;

        if (eventId != null && status != null) {
            reservations = reservationService.findByEventAndStatus(eventId, status);
        } else if (eventId != null) {
            reservations = reservationService.findByEventId(eventId);
        } else if (status != null) {
            reservations = reservationService.findByStatus(status);
        } else {
            reservations = reservationService.findAll();
        }

        model.addAttribute("events", allEvents);
        model.addAttribute("reservations", reservations);
        model.addAttribute("selectedEventId", eventId);
        model.addAttribute("selectedStatus", status);

        return "reservations/index";
    }


    @GetMapping("/reservation/create")
    public String create(@RequestParam(value = "eventId", required = false) Long eventId, Model model) {


        model.addAttribute("events", eventService.findAll());
        if (eventId != null) {
            Event selectedEvent = eventService.findById(eventId);
            model.addAttribute("selectedEventId", eventId);
            model.addAttribute("seatings", seatingService.findSeatingsByEvent(selectedEvent));
        }

        model.addAttribute("reservation", new Reservation());
        return "reservations/create";
    }

    @PostMapping("/reservation/create")
    public String create(@ModelAttribute @Valid Reservation reservation,
                         BindingResult bindingResult,
                         @RequestParam("eventId") String eventId,
                         @RequestParam("seatingId") String seatingId,
                         Model model) {

        model.addAttribute("events", eventService.findAll());

        Long selectedEventId = null;
        Long selectedSeatingId = null;

        try {
            if (eventId != null && !eventId.isBlank()) {
                selectedEventId = Long.parseLong(eventId);
                Event selectedEvent = eventService.findById(selectedEventId);
                model.addAttribute("seatings", seatingService.findSeatingsByEvent(selectedEvent));
            } else {
                bindingResult.addError(new FieldError("reservation", "seating", "Please select an event."));
            }

            if (seatingId != null && !seatingId.isBlank()) {
                selectedSeatingId = Long.parseLong(seatingId);
            } else {
                bindingResult.addError(new FieldError("reservation", "seating", "Seating selection is required."));
            }

        } catch (NumberFormatException e) {
            bindingResult.addError(new FieldError("reservation", "seating", "Invalid event or seating ID."));
        }

        model.addAttribute("selectedEventId", selectedEventId);
        model.addAttribute("selectedSeatingId", selectedSeatingId);

        if (bindingResult.hasErrors()) {
            return "reservations/create";
        }

        try {
            Seating selectedSeating = seatingService.findById(selectedSeatingId);
            reservation.setSeating(selectedSeating);
            reservation.setStatus(ReservationStatus.PENDING);
            reservationService.create(reservation);
            model.addAttribute("successMessage", "Reservation requested successfully!");
            model.addAttribute("reservation", new Reservation());
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "reservations/create";
        }

        return "reservations/create";
    }


    @GetMapping("/reservation/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Reservation reservation = reservationService.findById(id);

        DiningTable reservationDiningTable = reservation.getDiningTable();

        if (reservationDiningTable != null) {
            boolean isDiningTableArchived = reservationDiningTable.getIsArchived();
            model.addAttribute("isDiningTableArchived", isDiningTableArchived);
        }

        if (reservation == null) return "redirect:/reservations";

        model.addAttribute("reservation", reservation);

        Seating seating = reservation.getSeating();
        if (seating != null) {
            Event event = seating.getEvent();
            Layout layout = event.getLayout();
            LocalDateTime seatingDateTime = seating.getSeatingDateTime();

            List<DiningTable> availableTables = diningTableService.findAvailableDiningTables(
                    layout.getId(),
                    seatingDateTime
            );

            model.addAttribute("diningTables", availableTables);
        }

        return "reservations/detail";
    }


    @PostMapping("/reservation/confirm/{id}")
    public String confirmReservation(@PathVariable Long id,
                                     @RequestParam("status") String status,
                                     @RequestParam(value = "diningTableId", required = false) Long diningTableId,
                                     RedirectAttributes redirectAttributes) {

        Reservation reservation = reservationService.findById(id);
        if (reservation == null) return "redirect:/reservations";

        if ("DENY".equalsIgnoreCase(status)) {
            reservationService.setReservationStatus(id, ReservationStatus.DENIED);
            return "redirect:/reservations";
        } else if ("APPROVE".equalsIgnoreCase(status) && diningTableId == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a dining table before approving.");
            return "redirect:/reservation/" + id;
        }

        if (diningTableId != null) {
            if ("APPROVE".equalsIgnoreCase(status) && reservationService.canAccomodateGroupSize(reservation.getId(), diningTableId)) {
                reservationService.setReservationStatus(id, ReservationStatus.APPROVED);
                reservationService.setReservationDiningTable(id, diningTableId);
            } else {
                redirectAttributes.addFlashAttribute("errorMessageTable", "Dining Table cannot accomodate group size.");
                return "redirect:/reservation/" + id;
            }
        }

        return "redirect:/reservations";
    }
}

