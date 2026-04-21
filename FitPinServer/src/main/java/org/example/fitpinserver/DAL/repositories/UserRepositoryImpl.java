package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.UserPersistenceMapper;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJPARepository userJPARepository;
    private final UserPersistenceMapper userPersistenceMapper;

    public UserRepositoryImpl(UserJPARepository userJPARepository, UserPersistenceMapper userPersistenceMapper) {
        this.userJPARepository = userJPARepository;
        this.userPersistenceMapper = userPersistenceMapper;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userPersistenceMapper.toEntity(user);
        UserEntity savedEntity = userJPARepository.save(userEntity);
        return userPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<UserEntity> userEntity = userJPARepository.findById(id);
        if(userEntity.isPresent()){
            User user = userPersistenceMapper.toDomain(userEntity.get());
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Optional<UserEntity> userEntity = userJPARepository.findByUsername(username);
        if(userEntity.isPresent()){
            User user = userPersistenceMapper.toDomain(userEntity.get());
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmailAddress(String emailAddress) {
        Optional<UserEntity> userEntity = userJPARepository.findByEmailAddress(emailAddress);
        if(userEntity.isPresent()){
            User user = userPersistenceMapper.toDomain(userEntity.get());
            return Optional.of(user);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> usersEntities = userJPARepository.findAll();
        List<User> users = new ArrayList<>();
        for(UserEntity userEntity : usersEntities){
            users.add(userPersistenceMapper.toDomain(userEntity));
        }
        return users;
    }

    @Override
    public void deleteById(Long id) {
        userJPARepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJPARepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        return userJPARepository.existsByEmailAddress(emailAddress);
    }
}
