package nbcc.springresto.services;

public interface EmailSenderService {

    void sendEmail(String subject, String text, String from, String to);


}
