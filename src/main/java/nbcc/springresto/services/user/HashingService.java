package nbcc.springresto.services.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class HashingService {

    public String hash(String data) {
        var encoder = new BCryptPasswordEncoder();

        return encoder.encode(data);
    }

    public boolean valid(String plainText, String hashedValue) {
        var encoder = new BCryptPasswordEncoder();

        return encoder.matches(plainText, hashedValue);
    }
}
