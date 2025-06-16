package nbcc.springresto.services.user;

import jakarta.servlet.http.HttpSession;
import nbcc.springresto.entities.user.UserDetails;
import nbcc.springresto.entities.user.UserLogin;
import nbcc.springresto.repositories.user.UserLoginRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LoginServiceImpl implements LoginService {
    private static final String LOGIN_USER_ID = "loginUserId";
    private static final String IS_USER_LOGGED_IN = "loggedIn";
    private static final String LOGIN_ID = "loginId";

    private final UserLoginRepository userLoginRepository;
    private final UserService userService;
    private final HttpSession session;

    public LoginServiceImpl(UserLoginRepository userLoginRepository, UserService userService, HttpSession session) {
        this.userLoginRepository = userLoginRepository;
        this.userService = userService;
        this.session = session;
    }

    @Override
    public boolean login(String username, String password) {
        if (userService.valid(username, password)) {
            var user = userService.getUserByUsername(username);

            var userLogin = createNewUserLogin(user);

            session.setAttribute(LOGIN_ID, userLogin.getId());
            session.setAttribute(LOGIN_USER_ID, user.getId());
            session.setAttribute(IS_USER_LOGGED_IN, true);
            return true;
        }

        return false;
    }

    @Override
    public void logout() {
        var login = getCurrentLogin();

        if (login != null) {
            login.setLoggedOut(LocalDateTime.now());
            userLoginRepository.save(login);
        }
        session.removeAttribute(LOGIN_ID);
        session.removeAttribute(LOGIN_USER_ID);
        session.removeAttribute(IS_USER_LOGGED_IN);
    }

    @Override
    public boolean isCurrentLoginValid() {
        var user = getLoggedInUser();
        return user != null;
    }

    @Override
    public void updateLastUsed() {
        var login = getCurrentLogin();
        if (login != null) {
            login.setLastUsed(LocalDateTime.now());
            userLoginRepository.save(login);
        }
    }

    private UserLogin getCurrentLogin() {
        var loginIdAttribute = session.getAttribute(LOGIN_ID);
        if (loginIdAttribute == null) {
            return null;
        }

        return userLoginRepository.findById(loginIdAttribute.toString()).orElse(null);
    }

    private UserLogin createNewUserLogin(UserDetails userDetail) {
        var login = new UserLogin(userDetail);
        userLoginRepository.save(login);

        return login;
    }

    private UserDetails getLoggedInUser() {
        Long userId = getLoggedInUserId();

        if (userId == null) {
            return null;
        }

        return userService.findUser(userId).orElse(null);
    }

    private Long getLoggedInUserId() {
        var userIdAttribute = session.getAttribute(LOGIN_USER_ID);
        if (userIdAttribute == null) {
            return null;
        }

        var userIdString = userIdAttribute.toString();
        try {
            return Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
