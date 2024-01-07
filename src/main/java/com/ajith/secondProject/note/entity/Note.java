package com.ajith.secondProject.note.entity;

import com.ajith.secondProject.noteColor.entity.NoteBackGroundColor;
import com.ajith.secondProject.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long noteId;
        private String title;
        private String subTitle;
        private boolean isArchived = false;
        private boolean isDeleted = false;
    @ManyToOne
    @JoinColumn(name = "note_color_id")
        private NoteBackGroundColor noteColor;
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "user")
            private User user;
}
