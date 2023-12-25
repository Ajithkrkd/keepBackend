package com.ajith.secondProject.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/admin")
public class AdminController {

    @GetMapping()
    public ResponseEntity<String> sayAdmin(){
        return  ResponseEntity.ok("admin here");
    }
}
