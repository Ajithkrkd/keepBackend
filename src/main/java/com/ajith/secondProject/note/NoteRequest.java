package com.ajith.secondProject.note;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoteRequest {
    private String title;
    private String subTitle;
}
