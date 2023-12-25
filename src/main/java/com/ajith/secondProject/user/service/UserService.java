package com.ajith.secondProject.user.service;

import com.ajith.secondProject.user.Requests.UserDetailsUpdateRequest;
import com.ajith.secondProject.user.Response.UserDetailsResponse;


public interface UserService{
    UserDetailsResponse getUserDetails (String token);

    void updateUserDetails (String token, UserDetailsUpdateRequest userDetailsUpdateRequest);

    void blockUser (Long userId);

    boolean isEmailExist (String email);
}