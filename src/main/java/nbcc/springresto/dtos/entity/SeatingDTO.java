package nbcc.springresto.dtos.entity;

import java.time.LocalDateTime;

public class SeatingDTO {
    private Long id;
    private LocalDateTime seatingDateTime;
    private Integer durationInMinutes;

    public SeatingDTO(Long id, LocalDateTime seatingDateTime, Integer durationInMinutes) {
        this.id = id;
        this.seatingDateTime = seatingDateTime;
        this.durationInMinutes = durationInMinutes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getSeatingDateTime() {
        return seatingDateTime;
    }

    public void setSeatingDateTime(LocalDateTime seatingDateTime) {
        this.seatingDateTime = seatingDateTime;
    }

    public Integer getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Integer durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
