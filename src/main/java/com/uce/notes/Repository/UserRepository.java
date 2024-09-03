package com.uce.notes.Repository;

import com.uce.notes.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findByResetPasswordToken(String resetPasswordToken);
}

