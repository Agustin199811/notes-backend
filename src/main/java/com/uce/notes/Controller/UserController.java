package com.uce.notes.Controller;


import com.uce.notes.Model.User;
import com.uce.notes.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.createUser(user);
            return ResponseEntity.ok("Registration successful");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam("email") String email) {
        userService.generatePasswordResetToken(email);
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam("token") String token,
                                                @RequestParam("newPassword") String newPassword) {
        boolean result = userService.resetPassword(token, newPassword);
        if (result) {
            return ResponseEntity.ok("Password has been successfully reset.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllUser() {
        return ResponseEntity.ok(userService.getAllUser());
    }

}
