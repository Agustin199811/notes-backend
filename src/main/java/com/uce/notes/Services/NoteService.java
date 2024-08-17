package com.uce.notes.Services;

import com.uce.notes.Model.Note;
import com.uce.notes.Model.User;

import java.util.List;

public interface NoteService {
    Note createNoteForUser(Note note, String email);
    Note updateNoteForUser(Long noteId, Note note, String username);
    void deleteNoteForUser(Long id, String username);
    List<Note> getNotesForUser(String username);

}
