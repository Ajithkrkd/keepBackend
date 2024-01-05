package com.ajith.secondProject.note.repository;

import com.ajith.secondProject.note.entity.Note;
import com.ajith.secondProject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository< Note , Long > {
    @Query("SELECT n FROM Note n WHERE n.user = :user AND n.isDeleted = false AND n.isArchived = false")
    List<Note> findActiveNotesByUser(@Param("user") User user);

    List< Note> findAllNoteByUserIdAndIsDeletedTrue (Long userId);

    List< Note> findAllNoteByUserIdAndIsArchivedTrue (Long id);
}
