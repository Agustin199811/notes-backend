package com.uce.notes.Services.ServicesImp;

import com.uce.notes.Model.Dto.Mail;
import com.uce.notes.Model.Rol;
import com.uce.notes.Model.User;
import com.uce.notes.Repository.RolRepository;
import com.uce.notes.Repository.UserRepository;
import com.uce.notes.Services.MailService;
import com.uce.notes.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private MailService mailService;
    @Value("${reset.password}")
    private String resetPassword;

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

        User saveUser = userRepository.save(user);
        String subject = "User Registration Successful";
        String text = "Dear " + user.getName() + ",\n\nYour account has been created successfully.";
        Mail mail = new Mail(user.getEmail(), subject, text);
        mailService.sendEmail(mail);
        return saveUser;
    }

    @Override
    public void generatePasswordResetToken(String email) {
        Optional<User> userOptional = userRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = UUID.randomUUID().toString();
            user.setResetPasswordToken(token);
            user.setTokenExpirationTime(LocalDateTime.now().plusHours(1));
            userRepository.save(user);

            // Create the password reset URL
            String resetUrl = resetPassword + token;

            // Create an HTML message with the reset link
            String message = "<p>Click on the following link to reset your password:</p>" +
                    "<a href=\"" + resetUrl + "\">Reset Password</a>";

            // Create the Mail object with the HTML message
            Mail mail = new Mail(user.getEmail(), "Password Reset Request", message);
            //mail.setHtml(true); // Indicate that the content is HTML
            mailService.sendEmail(mail);
        }
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetPasswordToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getTokenExpirationTime().isAfter(LocalDateTime.now())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetPasswordToken(null);
                user.setTokenExpirationTime(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isResetTokenValid(String token) {
        Optional<User> userOptional = userRepository.findByResetPasswordToken(token);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getTokenExpirationTime().isAfter(LocalDateTime.now());
        }
        return false;
    }


    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
}