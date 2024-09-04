package com.uce.notes.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private boolean isEnabled;
    @ManyToMany(mappedBy = "tokens")
    @ToString.Exclude
    private Set<User> users;
}