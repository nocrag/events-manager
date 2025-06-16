package nbcc.springresto.services;

import jakarta.persistence.EntityNotFoundException;
import nbcc.springresto.entities.Menu;
import nbcc.springresto.entities.MenuItem;
import nbcc.springresto.repositories.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<Menu> findAll() {
        List<Menu> menus = menuRepository.findAll();
        menus.sort(Comparator.comparing(Menu::getName));
        return menus;
    }

    @Transactional
    public Menu create(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu update(Long id, Menu menu) {
        var dbOptionalMenu = menuRepository.findById(id);

        if (dbOptionalMenu.isEmpty()) {
            throw new EntityNotFoundException("Menu not found with id " + id);
        }
        var dbMenu = dbOptionalMenu.get();

        dbMenu.setName(menu.getName());
        dbMenu.setDescription(menu.getDescription());

        return menuRepository.save(dbMenu);
    }

    public boolean nameExists(String name) {
        return menuRepository.existsByName(name);
    }

    public Menu findById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        menuRepository.deleteById(id);
    }
}
