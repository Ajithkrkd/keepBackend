package com.ajith.secondProject.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/demo-controller")
public class AdminController {

    @GetMapping()
    public ResponseEntity<String> sayAdmin(){
        return  ResponseEntity.ok("admin here");
    }
}
