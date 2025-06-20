package nbcc.springresto.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import nbcc.springresto.entities.Menu;
import nbcc.springresto.entities.MenuItem;
import nbcc.springresto.services.EventService;
import nbcc.springresto.services.MenuItemService;
import nbcc.springresto.services.MenuService;
import nbcc.springresto.services.user.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MenuController {
    private final MenuService menuService;
    private final LoginService loginService;
    private final EventService eventService;
    private final MenuItemService menuItemService;

    public MenuController(MenuService menuService, LoginService loginService, EventService eventService, MenuItemService menuItemService) {
        this.menuService = menuService;
        this.loginService = loginService;
        this.eventService = eventService;
        this.menuItemService = menuItemService;
    }

    @ModelAttribute
    public void addLoggedInAttribute(Model model) {
        model.addAttribute("isLoggedIn", loginService.isCurrentLoginValid());
    }

    @GetMapping("/menus")
    public String getAll(Model model) {
        List<Menu> menus = menuService.findAll();

        model.addAttribute("menus", menus);
        return "menus/index";
    }

    @GetMapping("/menu/{id}")
    public String get(Model model, @PathVariable long id, MenuItem menuItem, HttpSession session) {
        Menu menu = menuService.findById(id);
        if (menu == null) {
            throw new RuntimeException("Menu not found for id: " + id);
        }

        String successMessage = (String) session.getAttribute("successMessage");
        if (successMessage != null) {
            model.addAttribute("successMessage", successMessage);
            session.removeAttribute("successMessage");
        }

        model.addAttribute("menu", menu);
        model.addAttribute("menuItem", menuItem);
        model.addAttribute("menuItems", menuItemService.findMenuItemsByMenu(menu));
        model.addAttribute("menuEvents", eventService.findAllByMenu(menu));
        return "menus/detail";
    }

    @GetMapping("/menu/create")
    public String create(Model model) {
        model.addAttribute("menu", new Menu());

        return "menus/create";
    }

    @PostMapping("/menu/create")
    public String create(@ModelAttribute @Valid Menu menu, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("menu", menu);
            return "menus/create";
        }

        if (menuService.nameExists(menu.getName().trim())) {
            model.addAttribute("errorMessage", menu.getName() + " already exists.");
            model.addAttribute("menu", menu);
            return "menus/create";
        }

        try {
            menuService.create(menu);

            return "redirect:/menus";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("menu", menu);
            return "menus/create";
        }
    }

    @GetMapping("/menu/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        Menu menu = menuService.findById(id);
        if (menu == null) {
            throw new RuntimeException("Menu not found for id: " + id);
        }

        model.addAttribute("menu", menu);
        return "menus/edit";
    }

    @PostMapping("/menu/edit/{id}")
    public String edit(@PathVariable long id, @ModelAttribute @Valid Menu menu,
                       BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("menu", menu);
            return "menus/edit";
        }

        if (menuService.nameExists(menu.getName().trim())) {
            model.addAttribute("errorMessage", menu.getName() + " already exists.");
            model.addAttribute("menu", menu);
            return "menus/edit";
        }

        try {
            menuService.update(id, menu);

            return "redirect:/menus";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("menu", menu);
            return "/menus/edit";
        }
    }

    @GetMapping("/menu/delete/{id}")
    public String delete(@PathVariable long id, Model model) {
        Menu menu = menuService.findById(id);
        if (menu == null) {
            throw new RuntimeException("Menu not found for id: " + id);
        }

        model.addAttribute("menu", menu);
        model.addAttribute("menuEvents", eventService.findAllByMenu(menu));
        return "menus/delete";
    }

    @PostMapping("/menu/delete/{id}")
    public String delete(Model model, @PathVariable long id) {
        Menu menu = menuService.findById(id);

        try {
            if (menu.getEvents() != null && !menu.getEvents().isEmpty()) {
                model.addAttribute("errorMessage", "Cannot delete this Menu because it has associated events.");
                model.addAttribute("menuEvents", eventService.findAllByMenu(menu));
                model.addAttribute("menu", menu);
                return "menus/delete";
            }
            menuService.deleteById(id);

            return "redirect:/menus";
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete menu with id: " + id, e);
        }
    }

    @GetMapping("/menu/{id}/menuItems")
    public String getMenuItems(@PathVariable long id, Model model) {
        Menu menu = menuService.findById(id);
        List<MenuItem> menuItems;

        menuItems = menuItemService.findMenuItemsByMenu(menu);

        model.addAttribute("menu", menu);
        model.addAttribute("menuItems", menuItems);
        return "menus/detail";
    }
}
