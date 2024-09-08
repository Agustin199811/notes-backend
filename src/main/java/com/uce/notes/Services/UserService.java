package com.uce.notes.Services;

import com.uce.notes.Model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    void generatePasswordResetToken(String email);
    boolean resetPassword(String token, String newPassword);
    boolean isResetTokenValid(String token);
    void deleteUser(Long userId);
    Optional<User> findUserByEmail(String email);
    List<User> getAllUser();
}
