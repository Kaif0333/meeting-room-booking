package com.kaif.meetingroombooking.controller;

import com.kaif.meetingroombooking.model.User;
import com.kaif.meetingroombooking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(service.createUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> list() {
        return ResponseEntity.ok(service.getAll());
    }
}
