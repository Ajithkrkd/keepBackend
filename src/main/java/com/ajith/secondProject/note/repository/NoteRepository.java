package com.ajith.secondProject.note.repository;

import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository< Note , Long > {
//    @Query ("SELECT * FROM Note  WHERE user = :userId AND n.isDeleted = false AND n.isArchived = false")
//    List<Note> findActiveNotesByUserId(@Param ("userId") User userId);

}
