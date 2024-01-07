package com.ajith.secondProject.noteColor.service;

import com.ajith.secondProject.noteColor.ColorResponse;
import org.springframework.http.ResponseEntity;

public interface NoteColorService {
    ResponseEntity< ColorResponse> getAllBackgroundColors ( );

    ResponseEntity<String> changeNotBackGroundColor (Integer colorId, Long noteId);
}
