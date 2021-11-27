package j3_l3.server;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUser(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
}
