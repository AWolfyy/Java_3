package j3_l4.Task_2.server;

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
