package nbcc.springresto.services.user;

import org.springframework.stereotype.Service;

@Service
public interface LoginService {
    boolean login(String username, String password);

    void logout();

    boolean isCurrentLoginValid();

    void updateLastUsed();
}
