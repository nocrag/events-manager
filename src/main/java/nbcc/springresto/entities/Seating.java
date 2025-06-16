package nbcc.springresto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Seating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Seating date and time are required.")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime seatingDateTime;

    @NotNull(message = "Seating duration is required.")
    @PositiveOrZero(message = "Invalid seating duration (in minutes).")
    private Integer durationInMinutes;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @UpdateTimestamp
    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "event_id", foreignKey = @ForeignKey(name = "FK_SEATING_EVENT"))
    private Event event;

    @OneToMany(mappedBy = "seating", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    public Seating() {
    }

    public Seating(Event event, LocalDateTime seatingDateTime, Integer durationInMinutes) {
        this.event = event;
        this.seatingDateTime = seatingDateTime;
        this.durationInMinutes = durationInMinutes;
    }


    public Long getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
