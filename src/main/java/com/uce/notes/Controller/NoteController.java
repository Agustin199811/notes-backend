package com.uce.notes.Controller;

import com.uce.notes.Model.Note;
import com.uce.notes.Services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    @Autowired
    private NoteService noteService;

    @PostMapping
    public Note createNote(@RequestBody Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return noteService.createNoteForUser(note, email);
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        noteService.deleteNoteForUser(id, email);
    }

    @PutMapping("/{id}")
    public Note updateNote(@PathVariable Long id, @RequestBody Note note) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return noteService.updateNoteForUser(id, note, email);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Note note = noteService.findByIdAndUserEmail(id, email);
        return ResponseEntity.ok(note);
    }

    @GetMapping
    public List<Note> getAllNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return noteService.getNotesForUser(email);
    }
}
