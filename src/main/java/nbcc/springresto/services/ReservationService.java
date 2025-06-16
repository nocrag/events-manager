package nbcc.springresto.services;


import jakarta.persistence.EntityNotFoundException;
import nbcc.springresto.config.EmailSenderConfig;
import nbcc.springresto.entities.DiningTable;
import nbcc.springresto.entities.Event;
import nbcc.springresto.entities.Reservation;
import nbcc.springresto.entities.Seating;
import nbcc.springresto.enums.ReservationStatus;
import nbcc.springresto.repositories.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final DiningTableService diningTableService;
    private final EmailSenderConfig sendEmailConfig;
    private final EmailSenderService emailSenderService;
    private final EmailSenderConfig emailSenderConfig;

    private final Logger log = LoggerFactory.getLogger(ReservationService.class);

    public ReservationService(ReservationRepository reservationRepository, DiningTableService diningTableService, EmailSenderConfig sendEmailConfig, EmailSenderService emailSenderService, EmailSenderConfig emailSenderConfig) {
        this.reservationRepository = reservationRepository;
        this.diningTableService = diningTableService;
        this.sendEmailConfig = sendEmailConfig;
        this.emailSenderService = emailSenderService;
        this.emailSenderConfig = emailSenderConfig;
    }

    public List<Reservation> findAll() {
        return reservationRepository.findAllByOrderBySeating_SeatingDateTimeAsc();
    }

    public Reservation findById(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found with ID: " + id));
    }

    public List<Reservation> findBySeating(Seating seating) {
        return reservationRepository.findBySeating(seating);
    }

    @Transactional
    public Reservation create(Reservation reservation) {
        reservation.setStatus(ReservationStatus.PENDING);
        var newReservation =  reservationRepository.save(reservation);
        sendReservationRequestEmail(newReservation);
        return newReservation;
    }

    public Reservation update(Long id, Reservation updatedReservation) {
        Reservation reservation = findById(id);
        //check for null

        reservation.setFirstName(updatedReservation.getFirstName());
        reservation.setLastName(updatedReservation.getLastName());
        reservation.setEmail(updatedReservation.getEmail());
        reservation.setGroupSize(updatedReservation.getGroupSize());
        reservation.setSeating(updatedReservation.getSeating());
        reservation.setStatus(updatedReservation.getStatus());

        return reservationRepository.save(reservation);
    }

    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> findByStatus(ReservationStatus status) {
        return reservationRepository.findByStatusOrderBySeating_SeatingDateTimeAsc(status);
    }

    public List<Reservation> findByEventId(Long eventId) {
        return reservationRepository.findBySeating_Event_IdOrderBySeating_SeatingDateTimeAsc(eventId);
    }

    public List<Reservation> findByEventAndStatus(Long eventId, ReservationStatus status) {
        return reservationRepository.findBySeating_Event_IdAndStatusOrderBySeating_SeatingDateTimeAsc(eventId, status);
    }


    public Reservation setReservationStatus(Long id, ReservationStatus status) {
        Reservation reservation = findById(id);

        if(reservation !=null) {
            reservation.setStatus(status);
            var reservationDecision = reservationRepository.save(reservation);
            sendReservationRequestEmail(reservationDecision);
        }
        return null;
    }

    public Reservation setReservationDiningTable(Long id, long diningTableId) {
        Reservation reservation = findById(id);
        DiningTable diningTableReservation = diningTableService.findById(diningTableId);

        if(reservation != null) {
            reservation.setDiningTable(diningTableReservation);
            return reservationRepository.save(reservation);
        }
        return null;
    }

    public boolean canAccomodateGroupSize(Long id, Long diningTableId) {

        Reservation reservation = findById(id);
        DiningTable diningTable = diningTableService.findById(diningTableId);

        int numberOfSeats = diningTable.getNumberOfSeats();
        int groupSize = reservation.getGroupSize();

        return numberOfSeats >= groupSize;
    }

    private void sendReservationRequestEmail(Reservation reservation) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy 'at' h:mm a");
        LocalDateTime seatingDateTime = reservation.getSeating().getSeatingDateTime();
        String formattedSeating = seatingDateTime.format(formatter);

        Event reservationEvent = reservation.getSeating().getEvent();
        String eventName = reservationEvent.getName();

        String reservationStatus = reservation.getStatus().toString();

        try{
            if(reservation.getEmail() != null && !reservation.getEmail().isBlank() ) {

                var subject = "";
                var subjectHeader = "";

                if (reservationStatus.equals("APPROVED")) {
                    subject = "Reservation Approved";
                    subjectHeader = "Get ready, your reservation is approved!";
                } else if (reservationStatus.equals("DENIED")) {
                    subject = "Reservation Denied";
                    subjectHeader =  "Unfortunately we cannot accommodate your reservation request." + "\n" +
                    "Please contact us at 506-556-334 if you'd like to check availability.";
                } else {
                    subject = "Reservation Requested";
                    subjectHeader = "Your reservation request has been received and it will be reviewed!";
                }

                String emailBody = subjectHeader + "\n\n" +
                        "Here are your reservation details: \n" +
                        "Name: " + reservation.getFirstName() + " " + reservation.getLastName() + "\n" +
                        "Event: " + eventName + "\n" +
                        "Seating Date and Time: " + formattedSeating + "\n" +
                        "Reservation status: " + reservation.getStatus() + "\n" +
                        "Group Size: " + reservation.getGroupSize() + "\n";
                emailSenderService.sendEmail(subject, emailBody, emailSenderConfig.getDefaultFrom(), reservation.getEmail());
            } else {
                log.warn("email not provided for user {}",  reservation.getFirstName());
            }
        } catch (Exception e) {
            log.error("Error while sending reservation request email", e);
        }
    }
}
