package com.example.usercashapi.DTO;

import com.example.usercashapi.domains.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class UserEmailDTO extends UserBase {
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    @JsonView(Views.Email.class)
    private String email;
}
