package com.ajith.secondProject.note.entity;

import com.ajith.secondProject.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        @JsonIgnore
        @ManyToOne
        @JoinColumn(name = "user")
            private User user;
}
