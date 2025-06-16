package nbcc.springresto.services.user;

import nbcc.springresto.entities.user.UserDetails;
import nbcc.springresto.repositories.user.UserDetailsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDetailsRepository userDetailsRepository;
    private final HashingService hashingService;

    public UserService(UserDetailsRepository userDetailsRepository, HashingService hashingService) {
        this.userDetailsRepository = userDetailsRepository;
        this.hashingService = hashingService;
    }

    public UserDetails register(UserDetails userDetails) {
        userDetails.setPassword(hashingService.hash(userDetails.getPassword()));
        return userDetailsRepository.save(userDetails);
    }

    public boolean valid(String username, String password) {
        var user = userDetailsRepository.getUserDetailsByUsername(username);

        if (user == null) {
            return false;
        }

        return hashingService.valid(password, user.getPassword());
    }

    public UserDetails getUserByUsername(String username) {
        return userDetailsRepository.getUserDetailsByUsername(username);
    }

    public Optional<UserDetails> findUser(Long id) {
        if (id == null) {
            return Optional.empty();
        }

        return userDetailsRepository.findById(id);
    }

    public boolean usernameExists(String username) {
        return userDetailsRepository.existsByUsername(username);
    }

    public boolean emailExists(String email) {
        return userDetailsRepository.existsByEmail(email);
    }
}
