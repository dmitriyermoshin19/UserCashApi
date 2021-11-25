package com.example.usercashapi.repositories;

import com.example.usercashapi.domains.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository  extends CrudRepository<Profile, Integer> {
}
