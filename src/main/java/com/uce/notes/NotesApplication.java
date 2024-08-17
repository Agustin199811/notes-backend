package com.uce.notes;

import com.uce.notes.Model.Rol;
import com.uce.notes.Model.User;
import com.uce.notes.Repository.RolRepository;
import com.uce.notes.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class NotesApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotesApplication.class, args);
    }

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private UserRepository userRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {

            Rol adminRole = rolRepository.findRolByName("ADMIN").orElseGet(() -> {
                Rol newRole = new Rol();
                newRole.setName("ADMIN");
                return rolRepository.save(newRole);
            });

            Rol userRole = rolRepository.findRolByName("USER").orElseGet(() -> {
                Rol newRole = new Rol();
                newRole.setName("USER");
                return rolRepository.save(newRole);
            });


        };
    }
}
