package nbcc.springresto.controllers;

import jakarta.validation.Valid;
import nbcc.springresto.entities.Layout;
import nbcc.springresto.repositories.DiningTableRepository;
import nbcc.springresto.repositories.LayoutRepository;
import nbcc.springresto.services.DiningTableService;
import nbcc.springresto.services.LayoutService;
import nbcc.springresto.services.user.LoginServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LayoutController {

    private final LayoutService layoutService;
    private final LayoutRepository layoutRepository;
    private final DiningTableService diningTableService;
    private final DiningTableRepository diningTableRepository;
    private final LoginServiceImpl loginService;


    public LayoutController(
            LayoutService layoutService,
            LayoutRepository layoutRepository,
            DiningTableService diningTableService,
            DiningTableRepository diningTableRepository, LoginServiceImpl loginService) {
        this.layoutService = layoutService;
        this.layoutRepository = layoutRepository;
        this.diningTableService = diningTableService;
        this.diningTableRepository = diningTableRepository;
        this.loginService = loginService;
    }

    @ModelAttribute
    public void addLoggedInAttribute(Model model) {
        model.addAttribute("isLoggedIn", loginService.isCurrentLoginValid());
    }

    @GetMapping("/layouts")
    public String getAll(Model model) {
        model.addAttribute("layouts", layoutService.findAll());
        return "/layouts/index";
    }

    @GetMapping("layout/create")
    public String create(Model model) {
        model.addAttribute("layout", new Layout());

        return "/layouts/create";
    }

    @GetMapping("/layout/{id}")
    public String get(Model model, @PathVariable long id) {

        Layout layout = layoutService.findById(id);

        if (layout == null) {
            return "redirect:/layouts";
        }

        model.addAttribute("layout", layout);
        model.addAttribute("diningTables", layout.getDiningTables());

        return "/layouts/detail";
    }

    @PostMapping("/layout/create")
    public String create(@Valid Layout layout, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/layouts/create";
        }

        layoutService.create(layout);
        return "redirect:/layouts";
    }

    @GetMapping("/layout/edit/{id}")
    public String edit(Model model, @PathVariable Long id) {
        var layout = layoutService.findById(id);

        if (layout == null) {
            return "redirect:/layouts";
        }

        var diningTables = layout.getDiningTables();
        model.addAttribute("layout", layout);
        model.addAttribute("diningTables", diningTables);
        return "/layouts/edit";

    }

    @GetMapping("/layout/{id}/dining-tables")
    public String getDiningTables(@PathVariable Long id, Model model) {
        Layout layout = layoutService.findById(id);
        model.addAttribute("layout", layout);
        model.addAttribute("diningTables", layout.getDiningTables());
        return "layouts/dining-tables";
    }

    @GetMapping("/layout/delete/{id}")
    public String delete(Model model, @PathVariable Long id) {

        Layout layout = layoutService.findById(id);

        if (layout == null) {
            return "redirect:/layouts";
        }

        model.addAttribute("layout", layout);
        return "/layouts/delete";
    }

    @GetMapping("/layouts/archived")
    public String getArchivedLayouts(Model model) {
        List<Layout> archivedLayouts = layoutService.findAllArchived();
        model.addAttribute("layouts", archivedLayouts);
        return "layouts/archived";
    }


    @PostMapping("/layout/edit")
    public String edit(@Valid Layout layout, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            return "/layouts/edit";
        }

        try {
            layoutService.update(layout.getId(), layout);
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "/layouts/edit";
        }

        return "redirect:/layouts";
    }

    @PostMapping("/layout/{layoutId}/dining-tables/add")
    public String addDiningTable(@PathVariable Long layoutId, @RequestParam int numberOfSeats) {
        diningTableService.addDiningTable(layoutId, numberOfSeats);
        return "redirect:/layout/edit/" + layoutId;
    }

    @PostMapping("/layout/dining-tables/{tableId}/delete")
    public String removeDiningTable(@PathVariable Long tableId, @RequestParam Long layoutId) {
        diningTableService.removeDiningTableFromLayout(tableId);
        return "redirect:/layout/" + layoutId;
    }

    @PostMapping("/layout/delete/{id}")
    public String delete(@PathVariable Long id) {

        layoutService.delete(id);

        return "redirect:/layouts";
    }


}
