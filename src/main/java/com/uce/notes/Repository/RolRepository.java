package com.uce.notes.Repository;

import com.uce.notes.Model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol,Long> {
    Optional<Rol> findRolByName(String name);
}