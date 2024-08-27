package com.uce.notes.Services.ServicesImp;

import com.uce.notes.Model.Note;
import com.uce.notes.Model.User;
import com.uce.notes.Repository.NoteRepository;
import com.uce.notes.Repository.UserRepository;
import com.uce.notes.Services.NoteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImp implements NoteService {
    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Note createNoteForUser(Note noteDto, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());
        note.setUser(user);

        return noteRepository.save(note);
    }

    public Note updateNoteForUser(Long noteId, Note note, String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Note existingNote = noteRepository.findById(noteId)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));
        existingNote.setContent(note.getContent());
        existingNote.setUser(user);
        return noteRepository.save(existingNote);
    }

    @Override
    public void deleteNoteForUser(Long id, String email) {
        User user = userRepository.findUserByEmail(email) // Adjust according to how you identify the user
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found"));

        if (note.getUser().equals(user)) {
            noteRepository.deleteById(id);
        } else {
            throw new SecurityException("User does not have permission to delete this note");
        }
    }

    @Override
    public List<Note> getNotesForUser(String username) {
        User user = userRepository.findUserByEmail(username) // Adjust according to how you identify the user
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        return noteRepository.findByUser(user); // Find notes by user
    }




}
