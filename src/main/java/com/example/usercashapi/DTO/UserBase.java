package com.example.usercashapi.DTO;

import com.example.usercashapi.domains.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public abstract class UserBase {
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @JsonView(Views.UpdateAge.class)
    String name;
}
