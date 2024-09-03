package com.uce.notes.Services;

import com.uce.notes.Model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    void generatePasswordResetToken(String email);
    boolean resetPassword(String token, String newPassword);
    boolean isResetTokenValid(String token);
    List<User> getAllUser();
}
