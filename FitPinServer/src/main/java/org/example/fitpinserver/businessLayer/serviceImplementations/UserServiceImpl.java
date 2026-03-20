package org.example.fitpinserver.businessLayer.serviceImplementations;
import org.example.fitpinserver.businessLayer.services.UserService;
import org.example.fitpinserver.domainLayer.entities.User;
import org.example.fitpinserver.dataAccessLayer.repositories.UserRepository;
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
