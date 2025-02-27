package com.example.ecommerce.services;

import com.example.ecommerce.models.AuthenticationProvider;
import com.example.ecommerce.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    User getInforUser(String email);
    void register(User user);
    User registerOAuth2User(String email, String name, AuthenticationProvider authenticationProvider);
    void updateUser(User user);
    void deleteUser(User user);

    List<User> getUserByEmail(String email);

    List < User > getUsers(String keyword);
    List < User > getUsersBlock(String keyword);

    void deleteUserByID(long id);
    void blockUserByID(long id);
    void unlockUserByID(long id);
}
