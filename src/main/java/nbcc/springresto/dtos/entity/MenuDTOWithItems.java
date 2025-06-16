package nbcc.springresto.dtos.entity;

import java.util.ArrayList;
import java.util.List;

public class MenuDTOWithItems {
    private Long id;

    private String menuName;

    private String menuDescription;

    private List<MenuItemDTO> items = new ArrayList<>();

    public MenuDTOWithItems(Long id, String menuName, String menuDescription, List<MenuItemDTO> items) {
        this.id = id;
        this.menuName = menuName;
        this.menuDescription = menuDescription;
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuDescription() {
        return menuDescription;
    }

    public void setMenuDescription(String menuDescription) {
        this.menuDescription = menuDescription;
    }

    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
    }
}
