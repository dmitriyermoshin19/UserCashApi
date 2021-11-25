package com.example.usercashapi.DTO;

import com.example.usercashapi.domains.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;

@Data
public class UserDTO extends UserBase {
    @JsonView(Views.User.class)
    private int id;
    @JsonView(Views.User.class)
    private Role role;
    @Min(value = 18, message = "age should be greater than 18")
    @JsonView(Views.User.class)
    private int age;
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @JsonView(Views.User.class)
    private String email;
    @NotEmpty(message = "Password should not be empty")
    @Size(min = 2, max = 60, message = "Password should be between 2 and 30 characters")
    @JsonView(Views.Create.class)
    private String password;
    //@NotNull
    @Valid
    @JsonView(Views.Create.class)
    private Profile profile;

    //@NotEmpty
    @JsonView(Views.Create.class)
    private Set<Phone> phones;

    public static UserDTO of(String name, String password, Role role) {
        UserDTO u = new UserDTO();
        u.name = name;
        u.role = role;
        u.password = password;
        return u;
    }

    public static UserDTO of(
            int id, String name, int age, String email, String password) {
        UserDTO u = new UserDTO();
        u.name = name;
        u.id = id;
        u.age = age;
        u.email = email;
        u.password = password;
        return u;
    }
//
//    public static UserDTO userByName(String name) {
//        var u = new UserDTO();
//        u.setName(name);
//        return u;
//    }

    public User toUser(){
        User user = new User();
        user.setName(name);
        user.setAge(age);
        user.setEmail(email);
        user.setProfile(profile);
        user.setPhones(phones);
        return user;
    }

    public static UserDTO fromUser(User user) {
        var userDto = new UserDTO();
        userDto.setId(user.getId());
        userDto.setRole(user.getRole());
        userDto.setName(user.getName());
        userDto.setAge(user.getAge());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    public static UserDTO fromCreateUser(User user) {
        var userDto = new UserDTO();
        userDto.setName(user.getName());
        userDto.setId(user.getId());
        userDto.setRole(user.getRole());
        userDto.setAge(user.getAge());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setProfile(user.getProfile());
        userDto.setPhones(user.getPhones());
        return userDto;
    }
}
