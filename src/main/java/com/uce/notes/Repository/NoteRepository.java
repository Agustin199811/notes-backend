package com.uce.notes.Repository;

import com.uce.notes.Model.Note;
import com.uce.notes.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByUser(User user);
}
