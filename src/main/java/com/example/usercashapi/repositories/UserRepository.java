package com.example.usercashapi.repositories;

import com.example.usercashapi.domains.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository  extends CrudRepository<User, Integer> {
    List<User> findByAge(int age);
    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}