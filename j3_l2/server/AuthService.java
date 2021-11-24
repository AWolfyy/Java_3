package j3_l2.server;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findUser(String login, String password) {
        return userRepository.findByLoginAndPassword(login, password);
    }
//    public static Set<Entry> entries = Set.of(
//            new Entry("Nick1", "l1", "p1"),
//            new Entry("Nick2", "l2", "p2"),
//            new Entry("Nick3", "l3", "p3")
//    );
//
//    public Optional<Entry> findEntryByCredentials(String login, String password) {
////        Iterator<Entry> iterator = entries.iterator();
////        while (iterator.hasNext()) {
////            Entry entry = iterator.next();
////            if (entry.getLogin().equals(login) && entry.getPassword().equals(password)) {
////                return Optional.of(entry);
////            }
////        }
////        return Optional.empty();
//
//        return entries.stream().
//                filter(entry -> entry.getLogin().equals(login) && entry.getPassword().equals(password)).
//                findFirst();
//    }
//
//    public static class Entry {
//        private final String nick;
//        private final String login;
//        private final String password;
//
//        public Entry(String nick, String login, String password) {
//            this.nick = nick;
//            this.login = login;
//            this.password = password;
//        }
//
//        public String getLogin() {
//            return login;
//        }
//
//        public String getPassword() {
//            return password;
//        }
//
//        public String getNick() {
//            return nick;
//        }
//    }
}
