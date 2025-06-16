package nbcc.springresto.controllers.api;

import jakarta.validation.Valid;
import nbcc.springresto.dtos.ValidationErrorsDTO;
import nbcc.springresto.dtos.entity.ReservationDTO;
import nbcc.springresto.entities.Seating;
import nbcc.springresto.services.ReservationService;
import nbcc.springresto.services.SeatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import static nbcc.springresto.dtos.DTOConverters.*;

@RestController
@RequestMapping("/api/reservation")
public class ReservationApiController {

    private final ReservationService reservationService;
    private final SeatingService seatingService;

    public ReservationApiController(ReservationService reservationService, SeatingService seatingService) {
        this.reservationService = reservationService;
        this.seatingService = seatingService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> create(@RequestBody @Valid ReservationDTO reservationDTO) {
        var reservationEntity = toReservation(reservationDTO, seatingService);
        Seating seating = seatingService.findById(reservationDTO.getSeatingId());

        if (seating == null) {
            String errorMessage = "Seating with ID: " + reservationDTO.getSeatingId() + " not found.";
            return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
        }

        Long actualEventId = seating.getEvent().getId();

        if (!actualEventId.equals(reservationDTO.getEventId())) {
            String errorMessage = "Seating ID " + reservationDTO.getSeatingId() + " does not belong to Event ID " + reservationDTO.getEventId();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        reservationEntity = reservationService.create(reservationEntity);
        return new ResponseEntity<>(toReservationDTO(reservationEntity), HttpStatus.CREATED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ValidationErrorsDTO<?>> validationErrorHandler(MethodArgumentNotValidException exception) {
        var errors = toValidationErrors(exception.getBindingResult());

        return new ResponseEntity<>(new ValidationErrorsDTO<>(exception.getTarget(), errors), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<String> exceptionHandler(Exception ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
