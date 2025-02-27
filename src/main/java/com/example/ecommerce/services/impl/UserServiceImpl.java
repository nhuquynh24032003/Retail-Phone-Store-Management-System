package com.example.ecommerce.services.impl;

import com.example.ecommerce.models.AuthenticationProvider;
import com.example.ecommerce.models.Role;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getInforUser(String email){
        return userRepository.findUserByEmail(email).orElseThrow();
    }

    @Override
    public void register(User user){
        List<User> users = userRepository.findAll();
        boolean hasRoleAdmin = false;
        for(User oneUser: users){
            if(oneUser.getRole() == Role.ROLE_ADMIN){
                hasRoleAdmin = true;
                break;
            }
        }
        if(users.size() == 0 || !hasRoleAdmin){
            user.setRole(Role.ROLE_ADMIN);
        }
        userRepository.save(user);
    }

    @Override
    public User registerOAuth2User(String email, String name, AuthenticationProvider authenticationProvider) {
        User user = new User(email, name, authenticationProvider);
        return userRepository.save(user);
    }

    @Override
    public void updateUser(User user){
        userRepository.save(user);
    }

    @Override
    public void deleteUser(User user){
        userRepository.delete(user);
    }

    @Override
    public List<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Override
    public List < User > getUsers(String keyword) {
        if (keyword != null) {
            return userRepository.search(keyword);
        }
        return userRepository.getUsers();
    }
    @Override
    public List < User > getUsersBlock(String keyword) {
        if (keyword != null) {
            return userRepository.searchBlock(keyword);
        }
        return userRepository.getUsersBlock(); }

    @Override
    public void deleteUserByID(long id) { userRepository.deleteById(id); }
    public void blockUserByID(long id) {
        Optional< User > optional = userRepository.findById((long) id);
        User user = null;
        if (optional.isPresent()) {
            user = optional.get();
            user.setStatus(false);
        }
        else {
            throw new RuntimeException("User not found for id : " + id);
        }

        userRepository.save(user);
    }
    public void unlockUserByID(long id) {
        Optional< User > optional = userRepository.findById((long) id);
        User user = null;
        if (optional.isPresent()) {
            user = optional.get();
            user.setStatus(true);
        }
        else {
            throw new RuntimeException("User not found for id : " + id);
        }

        userRepository.save(user);
    }

}
