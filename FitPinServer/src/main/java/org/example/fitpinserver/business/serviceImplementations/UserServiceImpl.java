package org.example.fitpinserver.business.serviceImplementations;
import org.example.fitpinserver.DAL.repositories.UserJPARepository;
import org.example.fitpinserver.DAL.repositories.UserRepositoryImpl;
import org.example.fitpinserver.business.services.UserService;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.domain.UserRepository;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public User registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername()))
            throw new RuntimeException("Username is already in use");

        if(userRepository.existsByEmailAddress(user.getEmailAddress()))
            throw new RuntimeException("Email is already in use");

        //I will add password hashing later for now its plain text

        return userRepository.save(user);
    }
}
