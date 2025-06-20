package nbcc.springresto.controllers;

import jakarta.validation.Valid;
import nbcc.springresto.entities.Event;
import nbcc.springresto.entities.Layout;
import nbcc.springresto.entities.Menu;
import nbcc.springresto.entities.Seating;
import nbcc.springresto.services.EventService;
import nbcc.springresto.services.LayoutService;
import nbcc.springresto.services.MenuService;
import nbcc.springresto.services.SeatingService;
import nbcc.springresto.services.user.LoginServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EventController {
    private final EventService eventService;
    private final SeatingService seatingService;
    private final LayoutService layoutService;
    private final LoginServiceImpl loginService;
    private final MenuService menuService;

    public EventController(EventService eventService, SeatingService seatingService, LayoutService layoutService,
                           LoginServiceImpl loginService, MenuService menuService) {
        this.eventService = eventService;
        this.seatingService = seatingService;
        this.layoutService = layoutService;
        this.loginService = loginService;
        this.menuService = menuService;
    }

    @ModelAttribute
    public void addLoggedInAttribute(Model model) {
        model.addAttribute("isLoggedIn", loginService.isCurrentLoginValid());
    }

    @GetMapping("/events")
    public String getAll(Model model,
                         @RequestParam(name = "filterDateOption", required = false) String filterDateOption,
                         @RequestParam(value = "startDate1", required = false) String startDate1Str,
                         @RequestParam(value = "startDate2", required = false) String startDate2Str) {
        List<Event> events;

        if ("all".equals(filterDateOption)) {
            events = eventService.findAll();
        } else if ("after".equals(filterDateOption) && startDate1Str != null && !startDate1Str.isEmpty()) {
            LocalDate startDate1 = LocalDate.parse(startDate1Str);
            events = eventService.findEventsAfterStartDate(startDate1);
        } else if ("before".equals(filterDateOption) && startDate1Str != null && !startDate1Str.isEmpty()) {
            LocalDate startDate1 = LocalDate.parse(startDate1Str);
            events = eventService.findEventsBeforeStartDate(startDate1);
        } else if ("between".equals(filterDateOption) &&
                startDate1Str != null && !startDate1Str.isEmpty() &&
                startDate2Str != null && !startDate2Str.isEmpty()) {
            LocalDate startDate1 = LocalDate.parse(startDate1Str);
            LocalDate startDate2 = LocalDate.parse(startDate2Str);
            events = eventService.findEventsBetweenDates(startDate1, startDate2);
        } else {
            events = eventService.findAll();
        }

        model.addAttribute("events", events);
        model.addAttribute("startDate1", startDate1Str);
        model.addAttribute("startDate2", startDate2Str);
        model.addAttribute("filterDateOption", filterDateOption);

        return "events/index";
    }


    @GetMapping("/event/create")
    public String create(Model model) {

        List<Layout> availableLayouts = layoutService.findAll();
        List<Menu> availableMenus = menuService.findAll();

        model.addAttribute("event", new Event());
        model.addAttribute("layouts", availableLayouts);
        model.addAttribute("menus", availableMenus);

        return "events/create";
    }

    @PostMapping("/event/create")
    public String create(@ModelAttribute @Valid Event event, BindingResult bindingResult, Model model,
                         @RequestParam("seatingDateTime") String seatingDateTimeStr,
                         @RequestParam(value = "durationInMinutes", defaultValue = "0") Integer durationInMinutes,
                         @RequestParam("layoutId") String layoutId,
                         @RequestParam("menuId") String menuId) {

        if (seatingDateTimeStr == null || seatingDateTimeStr.isEmpty()) {
            bindingResult.addError(new FieldError("event", "seatingDateTime",
                    "Seating date and time are required."));
        }
        if (durationInMinutes <= 0) {
            bindingResult.addError(new FieldError("event", "durationInMinutes",
                    "Seating duration is required."));
        }

        if (layoutId == null || layoutId.isEmpty()) {
            bindingResult.addError(new FieldError("event", "layout", "Please select a layout."));
        }

        if (menuId == null || menuId.isEmpty()) {
            bindingResult.addError(new FieldError("event", "menu", "Please choose a menu."));
        }

        if (bindingResult.hasErrors()) {
            StringBuilder seatingErrors = new StringBuilder();


            if (bindingResult.hasFieldErrors("seatingDateTime")) {
                seatingErrors.append(bindingResult.getFieldError("seatingDateTime").getDefaultMessage())
                        .append("<br>");
            }
            if (bindingResult.hasFieldErrors("durationInMinutes")) {
                seatingErrors.append(bindingResult.getFieldError("durationInMinutes").getDefaultMessage())
                        .append("<br>");
            }

            model.addAttribute("errorMessage", seatingErrors.toString());
            model.addAttribute("layouts", layoutService.findAll());
            model.addAttribute("menus", menuService.findAll());
            return "events/create";
        }

        try {
            Layout selectedLayout = layoutService.findById(Long.valueOf(layoutId));
            Menu selectedMenu = menuService.findById(Long.valueOf(menuId));

            if (selectedLayout.getIsArchived()) {
                throw new IllegalArgumentException("Invalid layout selection.");
            }

            event.setLayout(selectedLayout);
            event.setMenu(selectedMenu);

            Seating seating = new Seating();
            seating.setSeatingDateTime(LocalDateTime.parse(seatingDateTimeStr));
            seating.setDurationInMinutes(durationInMinutes);

            eventService.validateEventDates(event);
            seatingService.validateSeatingDates(event, seating);


            eventService.create(event);
            seating.setEvent(event);
            seatingService.create(seating);

        } catch (IllegalArgumentException e) { //separate validation errors from system errors
            bindingResult.addError(new FieldError("event", "layout", e.getMessage()));
            bindingResult.addError(new FieldError("event", "menu", e.getMessage()));
            model.addAttribute("layouts", layoutService.findAll());
            model.addAttribute("menus", layoutService.findAll());
            return "events/create";

        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("layouts", layoutService.findAll());
            model.addAttribute("menus", menuService.findAll());
            return "events/create";
        }

        return "redirect:/events";

    }

    @GetMapping("/event/{id}")
    public String get(Model model, @PathVariable long id) {
        Event event = eventService.findById(id);

        if (event == null) {
            return "redirect:/events";
        }

        model.addAttribute("event", event);
        model.addAttribute("seatings", seatingService.findSeatingsByEvent(event));
        model.addAttribute("isSameDayEvent", eventService.isSameDayEvent(event));
        model.addAttribute("seatingDates", seatingService.getEventSeatingDates(event));

        return "events/detail";
    }

    @GetMapping("/event/{id}/seatings")
    public String getSeatings(Model model, @PathVariable long id, @RequestParam(required = false) String selectedDate) {
        Event event = eventService.findById(id);
        List<Seating> seatings;

        if (selectedDate == null || selectedDate.isEmpty()) {
            seatings = seatingService.findSeatingsByEvent(event);
        } else {
            seatings = seatingService.findSeatingsByEventAndDate(event, LocalDate.parse(selectedDate));
        }

        model.addAttribute("event", event);
        model.addAttribute("seatings", seatings);
        model.addAttribute("selectedDate", selectedDate);
        model.addAttribute("isSameDayEvent", eventService.isSameDayEvent(event));
        model.addAttribute("seatingDates", seatingService.getEventSeatingDates(event));

        return "events/detail";
    }

    @GetMapping("/event/delete/{id}")
    public String delete(Model model, @PathVariable long id) {
        Event event = eventService.findById(id);
        model.addAttribute("currentDate", LocalDate.now());

        if (event != null && !event.isArchived()) {
            model.addAttribute("event", event);
            model.addAttribute("seatings", event.getSeatings());
            return "events/delete";
        }

        return "redirect:/events";
    }

    @PostMapping("/event/delete/{id}")
    public String delete(@PathVariable long id, Model model) {
        try {
            LocalDate currentDate = LocalDate.now();
            Event event = eventService.findById(id);
            if (event != null && event.getEndDate().isBefore(currentDate)) {
                event.setArchived(true);
                eventService.update(event.getId(), event);
                return "redirect:/events";
            } else {
                eventService.deleteById(id);
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            Event event = eventService.findById(id);
            if (event != null) {
                model.addAttribute("event", event);
                model.addAttribute("seatings", event.getSeatings());
            }
            return "events/delete";
        }

        return "redirect:/events";
    }

    @GetMapping("/event/{id}/edit")
    public String edit(Model model, @PathVariable long id) {
        List<Menu> availableMenus = menuService.findAll();
        List<Layout> availableLayouts = layoutService.findAllActive();
        Event event = eventService.findById(id);


        if (event == null) {
            return "redirect:/events";
        }

        Menu selectedMenu = menuService.findById(event.getMenu().getId());
        Layout selectedLayout = layoutService.findById(event.getLayout().getId());

        if (selectedLayout.getIsArchived()) {
            availableLayouts.add(selectedLayout);
        }

        model.addAttribute("event", event);
        model.addAttribute("menus", availableMenus);
        model.addAttribute("selectedMenu", selectedMenu);
        model.addAttribute("selectedLayout", selectedLayout);
        model.addAttribute("layouts", availableLayouts);
        model.addAttribute("seatings", seatingService.findSeatingsByEvent(event));
        return "events/edit";
    }


    @PostMapping("/event/{id}/edit")
    public String edit(@Valid Event event, BindingResult bindingResult,
                       @RequestParam("menuId") String menuId,
                       Model model,
                       @RequestParam("layoutId") String layoutId
    ) {
        Menu selectedMenu = menuService.findById(Long.valueOf(menuId));
        Layout selectedLayout = layoutService.findById(Long.valueOf(layoutId));

        if (bindingResult.hasErrors()) {
            List<Menu> availableMenus = menuService.findAll();
            List<Layout> availableLayouts = layoutService.findAllActive();

            if (selectedLayout.getIsArchived()) {
                availableLayouts.add(selectedLayout);
            }
            model.addAttribute("menus", availableMenus);
            model.addAttribute("selectedMenu", selectedMenu);
            model.addAttribute("layouts", availableLayouts);
            model.addAttribute("selectedLayout", selectedLayout);
            return "events/edit";
        }

        if (menuId == null || menuId.isEmpty()) {
            bindingResult.addError(new FieldError("event", "menu", "Please choose a menu."));
        }

        if (layoutId == null || layoutId.isEmpty()) {
            bindingResult.addError(new FieldError("event", "layout", "Please choose a layout."));
        }


        try {
            event.setMenu(selectedMenu);
            event.setLayout(selectedLayout);

            eventService.validateEventDates(event);
            eventService.validateEventSeatingDate(event);
            eventService.update(event.getId(), event);
        } catch (Exception e) {
            List<Menu> availableMenus = menuService.findAll();
            List<Layout> availableLayouts = layoutService.findAll();
            model.addAttribute("layouts", availableLayouts);
            model.addAttribute("menus", availableMenus);
            model.addAttribute("selectedMenu", selectedMenu);
            model.addAttribute("errorMessage", e.getMessage());
            return "events/edit";
        }

        return "redirect:/events";
    }
}
