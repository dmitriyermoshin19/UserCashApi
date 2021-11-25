package com.example.usercashapi.controllers;

import com.example.usercashapi.DTO.UserDTO;
import com.example.usercashapi.domains.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserController userController;

    @Test
    @WithMockUser
    void whenGetRequestFindAllThenReturnJSONs() throws Exception {
        var user1 = UserDTO.of(1, "user1", 23, "email@ya.ru",
                "password");
        when(userController.findAll()).thenReturn(Arrays.asList(user1));
        mockMvc.perform(get("/user/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("user1")))
                .andExpect(jsonPath("$[0].age", is(23)))
                .andExpect(jsonPath("$[0].email", is("email@ya.ru")));
//                .andExpect(jsonPath("$[0].password", is("password")));
        verify(userController, times(1)).findAll();
        verifyNoMoreInteractions(userController);
    }

    @Test
    @WithMockUser
    void whenGetRequestWithPresentedInDBIdThenReturnJson() throws Exception {
        var user1 = UserDTO.of(1, "user1", 23, "email@ya.ru",
                "password");
        when(userController.findById(1))
                .thenReturn(new ResponseEntity<UserDTO>(user1, HttpStatus.OK));
        mockMvc.perform(get("/user/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(5)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("user1")))
                .andExpect(jsonPath("$.age", is(23)))
                .andExpect(jsonPath("$.email", is("email@ya.ru")));
//                .andExpect(jsonPath("$.password", is("password")));
        verify(userController, times(1)).findById(anyInt());
        verifyNoMoreInteractions(userController);
    }

    @org.junit.jupiter.api.Test
    void create() {
    }

    @org.junit.jupiter.api.Test
    void delete() {
    }
}