package com.ajith.secondProject.controllers;

import com.ajith.secondProject.note.NoteRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/xyz/demo-controller")
public class DemoController {

    @GetMapping()
    public ResponseEntity<String> sayHello() {

        return ResponseEntity.ok("Hello from secured endpoint");
    }

}
