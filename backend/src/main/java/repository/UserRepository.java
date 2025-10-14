package com.kaif.meetingroombooking.repository;

import com.kaif.meetingroombooking.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findByEmail(String email);
}
