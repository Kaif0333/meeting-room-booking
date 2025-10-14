package com.kaif.meetingroombooking.service;

import com.kaif.meetingroombooking.model.User;
import com.kaif.meetingroombooking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository repo;
    public UserService(UserRepository repo) { this.repo = repo; }

    public User createUser(User u) { return repo.save(u); }
    public User findByEmail(String email) { return repo.findByEmail(email); }
    public List<User> getAll() { return repo.findAll(); }
}
