package com.ajith.secondProject.user.service;

import com.ajith.secondProject.user.Requests.UserDetailsUpdateRequest;
import com.ajith.secondProject.user.Response.UserDetailsResponse;
import com.ajith.secondProject.user.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


public interface UserService{
    UserDetailsResponse getUserDetails (String token);

    void updateUserDetails (String token, UserDetailsUpdateRequest userDetailsUpdateRequest);

    ResponseEntity < String > blockUser (Long userId);

    boolean isEmailExist (String email);

    Optional < User > findUserByName (String userName);
}