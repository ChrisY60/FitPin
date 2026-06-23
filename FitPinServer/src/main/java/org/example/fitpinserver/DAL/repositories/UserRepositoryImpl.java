package org.example.fitpinserver.DAL.repositories;

import org.example.fitpinserver.DAL.elasticsearch.UserDocument;
import org.example.fitpinserver.DAL.elasticsearch.UserSearchRepository;
import org.example.fitpinserver.DAL.entities.UserEntity;
import org.example.fitpinserver.DAL.mappers.UserPersistenceMapper;
import org.example.fitpinserver.business.repositories.UserRepository;
import org.example.fitpinserver.business.services.SearchIndexService;
import org.example.fitpinserver.domain.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJPARepository userJPARepository;
    private final UserPersistenceMapper userPersistenceMapper;
    private final SearchIndexService searchIndexService;
    private final UserSearchRepository userSearchRepository;

    public UserRepositoryImpl(UserJPARepository userJPARepository, UserPersistenceMapper userPersistenceMapper,
                              SearchIndexService searchIndexService, UserSearchRepository userSearchRepository) {
        this.userJPARepository = userJPARepository;
        this.userPersistenceMapper = userPersistenceMapper;
        this.searchIndexService = searchIndexService;
        this.userSearchRepository = userSearchRepository;
    }

    @Override
    public User save(User user) {
        UserEntity userEntity = userPersistenceMapper.toEntity(user);
        UserEntity savedEntity = userJPARepository.save(userEntity);
        User savedUser = userPersistenceMapper.toDomain(savedEntity);
        searchIndexService.indexUser(savedUser);
        return savedUser;
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
        searchIndexService.deleteUser(id);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJPARepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmailAddress(String emailAddress) {
        return userJPARepository.existsByEmailAddress(emailAddress);
    }

    @Override
    public User updateProfile(Long userId, String bio, String profilePictureUrl) {
        UserEntity entity = userJPARepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        entity.setBio(bio);
        entity.setProfilePictureUrl(profilePictureUrl);
        User updatedUser = userPersistenceMapper.toDomain(userJPARepository.save(entity));
        searchIndexService.indexUser(updatedUser);
        return updatedUser;
    }

    @Override
    public List<User> searchByUsername(String query) {
        return userSearchRepository.searchByUsername(query).stream()
                .map(UserRepositoryImpl::toDomain)
                .toList();
    }

    private static User toDomain(UserDocument document) {
        User user = new User(document.getId(), document.getUsername(), null, null, null);
        user.setProfilePictureUrl(document.getProfilePictureUrl());
        return user;
    }
}
