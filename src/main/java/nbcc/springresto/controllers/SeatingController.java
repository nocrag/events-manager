package nbcc.springresto.controllers;

import jakarta.validation.Valid;
import nbcc.springresto.entities.Event;
import nbcc.springresto.entities.Seating;
import nbcc.springresto.services.EventService;
import nbcc.springresto.services.SeatingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/event/{eventId}/seatings")
public class SeatingController {
    private final SeatingService seatingService;
    private final EventService eventService;

    public SeatingController(SeatingService seatingService, EventService eventService) {
        this.seatingService = seatingService;
        this.eventService = eventService;
    }

    @GetMapping("/new")
    public String create(@PathVariable Long eventId, Model model) {
        Event event = eventService.findById(eventId);

        Seating seating = new Seating();
        seating.setEvent(event);
        model.addAttribute("seating", seating);
        model.addAttribute("event", event);

        return "seatings/create";
    }

    @PostMapping("/new")
    public String create(@Valid Seating seating, BindingResult bindingResult, @PathVariable Long eventId, Model model) {
        Event event = eventService.findById(eventId);
        seating.setEvent(event);

        if (bindingResult.hasErrors()) {
            model.addAttribute("seating", seating);
            model.addAttribute("event", event);
            return "seatings/create";
        }

        try {
            seatingService.validateSeatingDates(event, seating);
            seatingService.create(seating);

            return "redirect:/event/" + eventId + "/seatings";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("seating", seating);
            model.addAttribute("event", event);
            return "seatings/create";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable long id) {
        Seating seating = seatingService.findById(id);
        Event event = seating.getEvent();


        model.addAttribute("event", event);
        model.addAttribute("seating", seating);

        return "/seatings/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        Seating seating = seatingService.findById(id);
        Event event = seating.getEvent();

        seatingService.deleteById(id);
        return "redirect:/event/" + event.getId() + "/seatings";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        Seating seating = seatingService.findById(id);
        Event event = seating.getEvent();

        model.addAttribute("seating", seating);
        model.addAttribute("event", event);

        return "seatings/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@Valid Seating seating, BindingResult bindingResult, @PathVariable Long eventId, @PathVariable Long id, Model model) {
        Seating existingSeating = seatingService.findById(id);
        Event event = eventService.findById(eventId);

        // set properties of the seating from the form submission
        existingSeating.setSeatingDateTime(seating.getSeatingDateTime());
        existingSeating.setDurationInMinutes(seating.getDurationInMinutes());

        if (bindingResult.hasErrors()) {
            model.addAttribute("seating", existingSeating);
            model.addAttribute("event", event);
            return "seatings/edit";
        }

        try {
            seatingService.validateSeatingDates(event, existingSeating);
            seatingService.update(existingSeating.getId(), existingSeating);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("seating", existingSeating);
            model.addAttribute("event", event);
            return "seatings/edit";
        }

        return "redirect:/event/" + eventId + "/seatings";
    }


}
