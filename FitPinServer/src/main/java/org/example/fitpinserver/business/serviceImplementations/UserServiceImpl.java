package org.example.fitpinserver.business.serviceImplementations;
import org.example.fitpinserver.business.services.UserService;
import org.example.fitpinserver.domain.repositories.UserRepository;
import org.example.fitpinserver.domain.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public User registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername()))
            throw new RuntimeException("Username is already in use");

        if(userRepository.existsByEmailAddress(user.getEmailAddress()))
            throw new RuntimeException("Email is already in use");

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String usernameOrEmail, String password) {
        User user = userRepository.findByUsername(usernameOrEmail).
                or(() -> userRepository.findByEmailAddress(usernameOrEmail))
                .orElseThrow(() -> new RuntimeException("Invalid username/email or password"));

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid username/email or password");
        }
        return user;
    }
}
