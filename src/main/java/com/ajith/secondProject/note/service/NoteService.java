package com.ajith.secondProject.note.service;

import com.ajith.secondProject.note.NoteRequest;
import com.ajith.secondProject.note.NoteResponse;
import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.user.entity.User;
import org.springframework.http.ResponseEntity;

import java.security.Principal;
import java.util.List;

public interface NoteService {
    void createNote (NoteRequest note ,String userName);

    List< Note> getAllNonDeletedAndNonArchivedNotes (User user);

    ResponseEntity<String> makeNoteDeleted (Long noteId);

    ResponseEntity<NoteResponse> getAllDeletedNotes (Principal principal);

    ResponseEntity< String> makeNoteArchived (Long noteId);

    ResponseEntity< NoteResponse> getAllArchivedNotes (Principal principal);
}
