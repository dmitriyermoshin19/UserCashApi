package com.example.usercashapi.controllers;

import com.example.usercashapi.DTO.*;
import com.example.usercashapi.domains.User;
import com.example.usercashapi.domains.Views;
import com.example.usercashapi.repositories.UserRepository;
import com.example.usercashapi.servises.UserService;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/user")
@Api( tags = "Users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @JsonView(Views.User.class)
    @GetMapping("/")
    public List<UserDTO> findAll() {
        return userService.findAllUserDTO();
    }

    @JsonView(Views.User.class)
    @GetMapping("/age/{age}")
    public List<UserDTO> findByAge(@PathVariable @Min(18) int age) {
        return userRepository.findByAge(age).stream().map(
                UserDTO::fromUser).collect(Collectors.toList());
    }

    @JsonView(Views.User.class)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable @Min(1) int id) {
        var user = userRepository.findById(id);
        if(user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var userDto = UserDTO.fromUser(user.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @JsonView(Views.User.class)
    @GetMapping("/phone/{number}")
    public ResponseEntity<UserDTO> findByPhone(
            @Valid @PathVariable @Length(min = 7, max = 10) String number) {
        var user = userService.findByPhone(number);
        if(user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var userDto = UserDTO.fromUser(user.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @JsonView(Views.User.class)
    @GetMapping("/name/{name}")
    public ResponseEntity<UserDTO> findByName(@PathVariable @Size(min = 2, max = 30) String name) {
        var user = userRepository.findByName(name);
        if(user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var userDto = UserDTO.fromUser(user.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @JsonView(Views.User.class)
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable @Email String email) {
        var user = userRepository.findByEmail(email);
        if(user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        var userDto = UserDTO.fromUser(user.get());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @JsonView(Views.Create.class)
    @PostMapping("/sign-up")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO user) {
        log.info(user + " Create... ");
        var userByEmail = userRepository.findByEmail(user.getEmail());
        if (userByEmail.isEmpty()) {
            var phoneIsPresent = user.getPhones().stream()
                    .filter(p -> userService.findByPhone(p.getValue()).isPresent()).findFirst();
            if (phoneIsPresent.isEmpty()) {
                return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
            } else {
                log.warn("User " + user + " not created, by reason: phone - " + phoneIsPresent
                        + " - already exists");
                return ResponseEntity.badRequest().build();
            }
        } else {
            log.warn("User " + user + " not created, by reason: email from "
                    + userByEmail.get().getEmail() + " already exists");
            return ResponseEntity.badRequest().build();
        }
    }

    @JsonView(Views.UpdateAge.class)
    @PutMapping("/age")
    public ResponseEntity<UserBase> updateAge(@Valid @RequestBody UserAgeDTO user) {
        var userDto =  userService.updateAge(user);
        return new ResponseEntity<>(userDto,
                userDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @JsonView(Views.Email.class)
    @PutMapping("/email")
    public ResponseEntity<UserBase> updateEmail(@Valid @RequestBody UserEmailDTO user) {
        var userDto = userService.updateEmail(user);
        return new ResponseEntity<>(userDto,
                userDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @JsonView(Views.UpdatePassword.class)
    @PutMapping("/password")
    public ResponseEntity<UserBase> updatePassword(
            @Valid @RequestBody UserPasswordDTO user) {
        var userDto =  userService.updatePassword(user);
        return new ResponseEntity<>(userDto,
                userDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @JsonView(Views.AddCash.class)
    @PutMapping("/add_cash")
    public ResponseEntity<UserBase> addCash(
            @Valid @RequestBody ProfileDTO user) {
        var userDto =  userService.addCash(user);
        return new ResponseEntity<>(userDto,
                userDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @JsonView(Views.Phone.class)
    @PutMapping("/add_phone")
    public ResponseEntity<UserBase> addPhone(@Valid @RequestBody PhoneDTO user) {
         var phoneDto = userService.addPhone(user);
        return new ResponseEntity<>(phoneDto,
                phoneDto != null ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Min(1) int id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
