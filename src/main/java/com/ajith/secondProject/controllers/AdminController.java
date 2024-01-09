package com.ajith.secondProject.controllers;

import com.ajith.secondProject.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserService userService;
    @GetMapping()
    public ResponseEntity<String> sayAdmin(){
        return  ResponseEntity.ok("admin here");
    }

    @PostMapping("/blockUser/{userId}")
    public ResponseEntity<String> makeBlockUser(@PathVariable Long userId)
    {
        return userService.blockUser ( userId );
    }
}
