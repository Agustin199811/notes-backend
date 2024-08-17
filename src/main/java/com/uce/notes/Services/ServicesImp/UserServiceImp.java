package com.uce.notes.Services.ServicesImp;

import com.uce.notes.Model.Rol;
import com.uce.notes.Model.User;
import com.uce.notes.Repository.RolRepository;
import com.uce.notes.Repository.UserRepository;
import com.uce.notes.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolRepository rolRepository;


    @Override
    public User createUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setAccountNoExpired(true);
        user.setAccountNoLocked(true);
        user.setCredentialNoExpired(true);
        Rol userRole = rolRepository.findRolByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));
        Set<Rol> roles = new HashSet<>();
        roles.add(userRole);
        user.setRols(roles);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}