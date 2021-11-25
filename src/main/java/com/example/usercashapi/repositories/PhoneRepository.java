package com.example.usercashapi.repositories;

import com.example.usercashapi.domains.Phone;
import org.springframework.data.repository.CrudRepository;

public interface PhoneRepository extends CrudRepository<Phone, Integer> {
}
