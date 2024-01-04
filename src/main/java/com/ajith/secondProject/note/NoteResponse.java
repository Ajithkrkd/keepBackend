package com.ajith.secondProject.note;

import com.ajith.secondProject.note.entity.Note;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NoteResponse {
    private String message;
    private List < Note > notes;


}
