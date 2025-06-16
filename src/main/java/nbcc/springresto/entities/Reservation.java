package nbcc.springresto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;
import nbcc.springresto.enums.ReservationStatus;

import java.util.List;

@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seating_id", nullable = false)
    private Seating seating;

    @ManyToOne
    @JoinColumn(name = "dining_table_id")
    @Valid
    private DiningTable diningTable;

    @NotBlank(message = "First Name is required and cannot be empty.")
    private String firstName;

    @NotBlank(message = "Last Name is required and cannot be empty.")
    private String lastName;

    @Email
    @NotBlank(message = "Email is required and cannot be empty.")
    private String email;

    @NotNull(message = "Group size is required.")
    @Min(value = 1, message = "Group size must be at least 1.")
    @Max(value = 20, message = "Group size must be at most 20.")
    private int groupSize;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;


    public Reservation() {
    }

    public Reservation(Seating seating, String firstName, String lastName, String email, int groupSize) {
        this.seating = seating;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupSize = groupSize;
        this.status = ReservationStatus.PENDING;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Seating getSeating() {
        return seating;
    }

    public void setSeating(Seating seating) {
        this.seating = seating;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public DiningTable getDiningTable() {
        return diningTable;
    }

    public void setDiningTable(DiningTable diningTable) {
        this.diningTable = diningTable;
    }
}
