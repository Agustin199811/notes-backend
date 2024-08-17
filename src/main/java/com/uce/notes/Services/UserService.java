package com.uce.notes.Services;

import com.uce.notes.Model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUser();
}
