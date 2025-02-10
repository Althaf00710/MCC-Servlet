package com.example.megacitycab.daos.interfaces;

import com.example.megacitycab.models.user.User;

import java.util.List;

public interface UserDAO {
    User getUserByUsername(String username);
    boolean addUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    List<User> getAllUsers();
    User getUserById(int userId);
    void updateLastActive(int userId, String lastActive);
    boolean updateAvatarUrl(User user);
    boolean isUsernameTaken(String username);
}
