package com.ajith.secondProject.note.service;

import com.ajith.secondProject.note.NoteRequest;
import com.ajith.secondProject.note.NoteResponse;
import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.note.repository.NoteRepository;
import com.ajith.secondProject.user.entity.User;
import com.ajith.secondProject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
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

    @Override
    public ResponseEntity<String> makeNoteDeleted(Long noteId) {
        Optional<Note> userNote = noteRepository.findById(noteId);

        if (userNote.isPresent()) {
            Note existingNote = userNote.get();
            existingNote.setDeleted(!existingNote.isDeleted());
            noteRepository.save(existingNote);

            return ResponseEntity.ok("Note " + (existingNote.isDeleted() ? "deleted" : "restored"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
        }
    }
    @Override
    public ResponseEntity < String > makeNoteArchived (Long noteId) {
        Optional<Note> userNote = noteRepository.findById(noteId);

        if (userNote.isPresent()) {
            Note existingNote = userNote.get();
            existingNote.setArchived (!existingNote.isArchived ());
            noteRepository.save(existingNote);

            return ResponseEntity.ok("Note " + (existingNote.isArchived () ? "archived" : "restored"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Note not found");
        }
    }

    @Override
    public ResponseEntity < NoteResponse > getAllArchivedNotes (Principal principal) {
        try {
            String userEmail = principal.getName();
            Optional<User> user = userRepository.findByEmail(userEmail);

            if (user.isPresent()) {
                User existingUser = user.get();
                List<Note> notes = noteRepository.findAllNoteByUserIdAndIsArchivedTrue (existingUser.getId());
                NoteResponse response = new NoteResponse();
                response.setNotes(notes);
                response.setMessage("Successfully retrieved the archived notes.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                NoteResponse response = new NoteResponse();
                response.setMessage("User does not exist.");
                response.setNotes(new ArrayList<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            NoteResponse response = new NoteResponse();
            response.setMessage("Internal Server Error");
            response.setNotes(new ArrayList<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @Override
    public ResponseEntity<NoteResponse> getAllDeletedNotes (Principal principal) {
        try {
            String userEmail = principal.getName();
            Optional<User> user = userRepository.findByEmail(userEmail);

            if (user.isPresent()) {
                User existingUser = user.get();
                List<Note> notes = noteRepository.findAllNoteByUserIdAndIsDeletedTrue (existingUser.getId());
                NoteResponse response = new NoteResponse();
                response.setNotes(notes);
                response.setMessage("Successfully retrieved the deleted notes.");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                NoteResponse response = new NoteResponse();
                response.setMessage("User does not exist.");
                response.setNotes(new ArrayList<>());
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            NoteResponse response = new NoteResponse();
            response.setMessage("Internal Server Error");
            response.setNotes(new ArrayList<>());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
