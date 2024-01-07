package com.ajith.secondProject.noteColor;

import com.ajith.secondProject.noteColor.entity.NoteBackGroundColor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ColorResponse {
    private String message;
    private List < NoteBackGroundColor > colors;
}
