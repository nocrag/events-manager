package nbcc.springresto.controllers;

import jakarta.validation.Valid;
import nbcc.springresto.entities.DiningTable;
import nbcc.springresto.entities.Layout;
import nbcc.springresto.repositories.DiningTableRepository;
import nbcc.springresto.services.DiningTableService;
import nbcc.springresto.services.LayoutService;
import nbcc.springresto.services.user.LoginServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DiningTableController {

    private final DiningTableService diningTableService;
    private final LayoutService layoutService;
    private final DiningTableRepository diningTableRepository;
    private final LoginServiceImpl loginService;

    @ModelAttribute
    public void addLoggedInAttribute(Model model) {
        model.addAttribute("isLoggedIn", loginService.isCurrentLoginValid());
    }

    public DiningTableController(DiningTableService diningTableService, LayoutService layoutService, DiningTableRepository diningTableRepository, LoginServiceImpl loginService) {
        this.diningTableService = diningTableService;
        this.layoutService = layoutService;
        this.diningTableRepository = diningTableRepository;
        this.loginService = loginService;
    }

    @ModelAttribute("layouts")
    public List<Layout> getAllLayouts() {
        return layoutService.findAll();
    }

    @GetMapping("/dining-tables")
    public String getAll(Model model) {
        model.addAttribute("diningTables", diningTableService.findAllActive());
        return "dining-tables/index";
    }

    @GetMapping("/dining-table/create")
    public String create(Model model) {
        model.addAttribute("diningTable", new DiningTable());

        return "dining-tables/create";
    }

    @GetMapping("/dining-tables/delete/{id}")
    public String showDeleteConfirmation(@PathVariable Long id, Model model) {
        DiningTable diningTable = diningTableRepository.findById(id).orElse(null);

        if (diningTable == null) {
            return "redirect:/dining-tables";
        }

        model.addAttribute("diningTable", diningTable);
        return "dining-tables/delete";
    }

    @GetMapping("/dining-tables/archived")
    public String getArchivedDiningTables(Model model) {
        List<DiningTable> archivedDiningTables = diningTableService.findAllArchived();
        model.addAttribute("diningTables", archivedDiningTables);
        return "dining-tables/archived";
    }


    @PostMapping("/dining-table/create")
    public String create(@Valid DiningTable diningTable, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "dining-tables/create";
        }

        diningTableService.save(diningTable);
        return "redirect:/dining-tables";
    }


    @PostMapping("/dining-tables/delete/{id}")
    public String delete(@PathVariable Long id) {

        diningTableService.delete(id);

        return "redirect:/dining-tables";
    }

}
