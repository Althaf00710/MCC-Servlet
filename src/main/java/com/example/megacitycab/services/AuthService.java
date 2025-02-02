package com.example.megacitycab.services;

import com.example.megacitycab.daos.impl.UserDAOImpl;
import com.example.megacitycab.models.user.User;
import com.example.megacitycab.utils.PasswordHasher;

public class AuthService {
    private final UserDAOImpl userDao = new UserDAOImpl();

    public User authenticate(String username, String rawPassword) {
        User user = userDao.getUserByUsername(username);
        if (user != null && PasswordHasher.verify(rawPassword, user.getPassword())) {
            return user; // Valid credentials
        }
        return null; // Invalid
    }
}
