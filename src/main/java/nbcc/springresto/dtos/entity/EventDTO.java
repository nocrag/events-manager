package nbcc.springresto.dtos.entity;

import java.time.LocalDate;
import java.util.List;

public class EventDTO {

    private Long id;

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double price;

    private List<SeatingDTO> seatings;

    private MenuDTO menu;

    public EventDTO() {
    }

    public EventDTO(Long id, String name, String description, LocalDate startDate, LocalDate endDate, Double price,
                    List<SeatingDTO> seatings, MenuDTO menu) {
        this.id = id;
        this.name = name;
        this.description = (description != null && !description.trim().isEmpty()) ? description : "N/A";
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.seatings = seatings;
        this.menu = menu;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<SeatingDTO> getSeatings() {
        return seatings;
    }

    public void setSeatings(List<SeatingDTO> seatings) {
        this.seatings = seatings;
    }

    public MenuDTO getMenu() {
        return menu;
    }

    public void setMenu(MenuDTO menu) {
        this.menu = menu;
    }
}
