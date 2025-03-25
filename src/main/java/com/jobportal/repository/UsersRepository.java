package com.jobportal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.jobportal.entity.User;
import java.util.Optional;


public interface UsersRepository  extends MongoRepository<User, Long>{
    public Optional<User>  findByEmail(String email);

}
