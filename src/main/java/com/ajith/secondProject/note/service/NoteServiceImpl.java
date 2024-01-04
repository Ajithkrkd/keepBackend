package com.ajith.secondProject.note.service;

import com.ajith.secondProject.note.NoteRequest;
import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.note.repository.NoteRepository;
import com.ajith.secondProject.user.entity.User;
import com.ajith.secondProject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteServiceImpl implements NoteService{

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    @Override
    public void createNote (NoteRequest note ,String userName) {
        Optional < User > user = userRepository.findByEmail ( userName );
        if(user.isPresent ()) {
        Note newNote = new Note();
        newNote.setTitle ( note.getTitle() );
        newNote.setSubTitle ( note.getSubTitle() );
        newNote.setUser (user.get());
        noteRepository.save ( newNote );
        }
    }

    @Override
    public List < Note > getAllNonDeletedAndNonArchivedNotes (User user) {
        return noteRepository.findActiveNotesByUser ( user );
    }
}
