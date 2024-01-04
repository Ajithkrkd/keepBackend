package com.ajith.secondProject.note.service;

import com.ajith.secondProject.note.NoteRequest;
import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.user.entity.User;

import java.util.List;

public interface NoteService {
    void createNote (NoteRequest note ,String userName);

    List< Note> getAllNonDeletedAndNonArchivedNotes ();
}
