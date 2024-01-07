package com.ajith.secondProject.noteColor.service;

import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.note.repository.NoteRepository;
import com.ajith.secondProject.noteColor.ColorResponse;
import com.ajith.secondProject.noteColor.entity.NoteBackGroundColor;
import com.ajith.secondProject.noteColor.repository.NoteColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoteColorServiceImpl implements NoteColorService {


    private  final NoteColorRepository noteColorRepository;
    private  final NoteRepository noteRepository;
    @Override
    public ResponseEntity < ColorResponse > getAllBackgroundColors ( ) {

        List < NoteBackGroundColor > colors =  noteColorRepository.findAll ();
        ColorResponse colorResponse = new ColorResponse();
        colorResponse.setColors ( colors );
        colorResponse.setMessage ( "successfully retrieved colors" );
         return ResponseEntity.ok(colorResponse);
    }

    @Override
    public ResponseEntity<String> changeNotBackGroundColor(Integer colorId, Long noteId) {
        try {
            Optional<Note> note = noteRepository.findById(noteId);
            Optional<NoteBackGroundColor> color = noteColorRepository.findById(colorId);

            if (note.isPresent()) {
                Note existingNote = note.get();

                if (color.isPresent()) {
                    existingNote.setNoteColor(color.get());
                    noteRepository.save(existingNote);
                    return ResponseEntity.ok("Note color changed successfully");
                } else {
                    return ResponseEntity.status( HttpStatus.NOT_FOUND)
                            .body("Note background color with id " + colorId + " not found");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Note with id " + noteId + " not found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while processing the request");
        }
    }

}
