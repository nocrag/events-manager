package nbcc.springresto.dtos.entity;

import jakarta.validation.constraints.*;

public class ReservationDTO {

    @NotNull(message = "Event is required.")
    private Long eventId;

    @NotNull(message = "Seating is required.")
    private Long seatingId;

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
    private Integer groupSize;

    public ReservationDTO() {
    }

    public ReservationDTO(Long eventId, Long seatingId,
                          String firstName, String lastName, String email,
                          Integer groupSize) {
        this.eventId = eventId;
        this.seatingId = seatingId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.groupSize = groupSize;
    }

    public @NotNull(message = "Event is required.") Long getEventId() {
        return eventId;
    }

    public void setEventId(@NotNull(message = "Event is required.") Long eventId) {
        this.eventId = eventId;
    }

    public @NotNull(message = "Seating is required.") Long getSeatingId() {
        return seatingId;
    }

    public void setSeatingId(@NotNull(message = "Seating is required.") Long seatingId) {
        this.seatingId = seatingId;
    }

    public @NotBlank(message = "First Name is required and cannot be empty.") String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotBlank(message = "First Name is required and cannot be empty.") String firstName) {
        this.firstName = firstName;
    }

    public @NotBlank(message = "Last Name is required and cannot be empty.") String getLastName() {
        return lastName;
    }

    public void setLastName(@NotBlank(message = "Last Name is required and cannot be empty.") String lastName) {
        this.lastName = lastName;
    }

    public @Email @NotBlank(message = "Email is required and cannot be empty.") String getEmail() {
        return email;
    }

    public void setEmail(@Email @NotBlank(message = "Email is required and cannot be empty.") String email) {
        this.email = email;
    }

    public @NotNull(message = "Group size is required.") @Min(value = 1, message = "Group size must be at least 1.") Integer getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(@NotNull(message = "Group size is required.") @Min(value = 1, message = "Group size must be at least 1.") Integer groupSize) {
        this.groupSize = groupSize;
    }
}
