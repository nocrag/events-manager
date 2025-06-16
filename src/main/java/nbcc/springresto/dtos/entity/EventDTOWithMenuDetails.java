package nbcc.springresto.dtos.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventDTOWithMenuDetails {

    private Long id;

    private String eventName;

    private String eventDescription;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double price;

    private MenuDTO menu;

    private List<MenuItemDTO> items = new ArrayList<>();

    public EventDTOWithMenuDetails() {
    }

    public EventDTOWithMenuDetails(Long id, String name, String description, LocalDate startDate, LocalDate endDate, Double price,
                                   MenuDTO menu, List<MenuItemDTO> items) {
        this.id = id;
        this.eventName = name;
        this.eventDescription = (description != null && !description.trim().isEmpty()) ? description : "N/A";
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.menu = menu;
        this.items = items;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }

    public List<MenuItemDTO> getItems() {
        return items;
    }

    public void setItems(List<MenuItemDTO> items) {
        this.items = items;
    }
}
