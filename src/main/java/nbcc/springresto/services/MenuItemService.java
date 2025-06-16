package nbcc.springresto.services;

import nbcc.springresto.entities.Menu;
import nbcc.springresto.entities.MenuItem;
import nbcc.springresto.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem create(MenuItem menuItem) {
        return menuItemRepository.save(menuItem);
    }

    public MenuItem update(Long id, MenuItem menuItem) {
        var dbOptionalMenuItem = menuItemRepository.findById(id);

        if (dbOptionalMenuItem.isEmpty()) {
            throw new RuntimeException("Menu item not found with id: " + id);
        }
        var dbMenuItem = dbOptionalMenuItem.get();

        dbMenuItem.setName(menuItem.getName());
        dbMenuItem.setDescription(menuItem.getDescription());
        dbMenuItem.setMenu(menuItem.getMenu());

        return menuItemRepository.save(dbMenuItem);
    }

    public MenuItem findById(Long id) {
        return menuItemRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    public List<MenuItem> findMenuItemsByMenu(Menu menu) {
        List<MenuItem> menuItems = menuItemRepository.findByMenu(menu);
        menuItems.sort(Comparator.comparing(MenuItem::getName));
        return menuItems;
    }


    public boolean nameExists(String name, Menu menu) {
        return menuItemRepository.existsByNameAndMenu(name, menu);
    }
}
