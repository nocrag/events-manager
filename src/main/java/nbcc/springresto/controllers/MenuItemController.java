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
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/menu/{menuId}/items")
public class MenuItemController {
    private final MenuItemService menuItemService;
    private final MenuService menuService;
    private final EventService eventService;
    private final LoginService loginService;


    public MenuItemController(MenuItemService menuItemService, MenuService menuService, EventService eventService,
                              LoginService loginService) {
        this.menuItemService = menuItemService;
        this.menuService = menuService;
        this.eventService = eventService;
        this.loginService = loginService;
    }

    @ModelAttribute
    public void addLoggedInAttribute(Model model) {
        model.addAttribute("isLoggedIn", loginService.isCurrentLoginValid());
    }

    @PostMapping("/add")
    public String create(@Valid MenuItem menuItem, BindingResult bindingResult,
                         @PathVariable Long menuId, Model model, HttpSession session) {
        Menu menu = menuService.findById(menuId);
        menuItem.setMenu(menu);

        if (bindingResult.hasErrors()) {
            model.addAttribute("menuItem", menuItem);
            model.addAttribute("menu", menu);
            model.addAttribute("menuItems", menuItemService.findMenuItemsByMenu(menu));
            model.addAttribute("menuEvents", eventService.findAllByMenu(menu));
            return "menus/detail";
        }

        if (menuItemService.nameExists(menuItem.getName().trim(), menu)) {
            model.addAttribute("errorMessage", menuItem.getName() + " already exists.");
            model.addAttribute("menuItem", menuItem);
            model.addAttribute("menu", menu);
            model.addAttribute("menuItems", menuItemService.findMenuItemsByMenu(menu));
            model.addAttribute("menuEvents", eventService.findAllByMenu(menu));
            return "menus/detail";
        }

        try {
            menuItemService.create(menuItem);
            session.setAttribute("successMessage", menuItem.getName() + " added successfully!");
            return "redirect:/menu/" + menuId;
        } catch (Exception e) {
            return "menus/detail";
        }
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable long id, Model model) {
        MenuItem menuItem = menuItemService.findById(id);
        Menu menu = menuItem.getMenu();

        model.addAttribute("menuItem", menuItem);
        model.addAttribute("menu", menu);

        return "menuItems/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@ModelAttribute @Valid MenuItem menuItem, BindingResult bindingResult,
                       @PathVariable Long menuId, Model model) {
        Menu menu = menuService.findById(menuId);
        menuItem.setMenu(menu);

        model.addAttribute("menu", menu);

        if (bindingResult.hasErrors()) {
            model.addAttribute("menuItem", menuItem);
            return "menuItems/edit";
        }

        if (menuItemService.nameExists(menuItem.getName().trim(), menu)) {
            model.addAttribute("errorMessage", menuItem.getName() + " already exists.");
            model.addAttribute("menuItem", menuItem);
            return "menuItems/edit";
        }

        try {
            menuItemService.update(menuItem.getId(), menuItem);

            return "redirect:/menu/" + menuId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("menuItem", menuItem);
            return "menuItems/edit";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable long id) {
        MenuItem menuItem = menuItemService.findById(id);
        Menu menu = menuItem.getMenu();

        model.addAttribute("menuItem", menuItem);
        model.addAttribute("menu", menu);

        return "menuItems/delete";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        MenuItem menuItem = menuItemService.findById(id);
        Menu menu = menuItem.getMenu();

        menuItemService.deleteById(id);
        return "redirect:/menu/" + menu.getId();
    }
}
