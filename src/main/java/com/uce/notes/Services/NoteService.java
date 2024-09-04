package com.uce.notes.Services;

import com.uce.notes.Model.Note;
import com.uce.notes.Model.User;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    Note createNoteForUser(Note note, String email);
    Note updateNoteForUser(Long noteId, Note note, String email);
    void deleteNoteForUser(Long id, String email);
    List<Note> getNotesForUser(String email);
    Note findByIdAndUserEmail(Long id, String email);

}
