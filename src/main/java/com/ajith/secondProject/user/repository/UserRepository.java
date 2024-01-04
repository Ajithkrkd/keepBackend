package com.ajith.secondProject.user.repository;

import com.ajith.secondProject.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository< User,Long> {
    Optional <User> findByEmail(String email);
    boolean existsByEmail (String email);
}
