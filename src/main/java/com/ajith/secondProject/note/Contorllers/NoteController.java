package com.ajith.secondProject.note.Contorllers;

import com.ajith.secondProject.note.NoteRequest;
import com.ajith.secondProject.note.NoteResponse;
import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.note.service.NoteService;
import com.ajith.secondProject.user.entity.User;
import com.ajith.secondProject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;
    @PostMapping ("/createNote")
    public ResponseEntity<String> createNote(@RequestBody NoteRequest note, Principal principal) {
        try {
            String userName = principal.getName ();
            noteService.createNote(note ,userName);
            return new ResponseEntity<>("NoteRequest saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to save note", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getNote")
    public ResponseEntity<NoteResponse> getAllNonDeletedAndNonArchivedNotes(Principal principal) {

        try {
            String userName = principal.getName ();
            Optional < User > user = userService.findUserByName(userName);
            List < Note > nonDeletedAndNonArchivedNotes = noteService.getAllNonDeletedAndNonArchivedNotes (user.get());
            NoteResponse noteResponse = new NoteResponse();
            noteResponse.setMessage("Successfully retrieved notes");
            noteResponse.setNotes(nonDeletedAndNonArchivedNotes);
            return ResponseEntity.ok(noteResponse);
        }
        catch (Exception e){
            NoteResponse errorResponse = new NoteResponse();
            errorResponse.setMessage("Successfully retrieved notes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

    }

    @PostMapping("/note/delete/{noteId}")
    public ResponseEntity<String> makeNoteDeleted(@PathVariable Long noteId)
    {
        return noteService.makeNoteDeleted(noteId);
    }

    @GetMapping("/note/getAllDeletedNotes")
    public ResponseEntity<NoteResponse> getAllDeletedNotes(Principal principal){
        return noteService.getAllDeletedNotes(principal);
    }
}
