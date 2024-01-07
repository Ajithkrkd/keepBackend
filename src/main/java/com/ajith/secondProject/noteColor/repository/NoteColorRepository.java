package com.ajith.secondProject.noteColor.repository;
import com.ajith.secondProject.noteColor.entity.NoteBackGroundColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteColorRepository extends JpaRepository < NoteBackGroundColor,Integer > {

}
