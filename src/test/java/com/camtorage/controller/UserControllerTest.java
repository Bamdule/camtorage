package com.camtorage.controller;

import com.camtorage.entity.user.UserTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Test
    public void joinUserSuccessTest() throws Exception {
        UserTO user = UserTO.builder()
                .email("test1@gmail.com")
                .password("1234")
                .build();

        this.mockMvc.perform(post("/api/user")
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName("회원가입 시 잘못된 파라메터를 넘길 경우")
    public void joinUserInvalidParameterTest() throws Exception {
        UserTO user = UserTO.builder()
                .email("test@gmail.com")
                .password("1234")
                .build();

        this.mockMvc.perform(post("/api/user")
                .param("email", "")
                .param("password", user.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("회원 로그인 성공")
    public void userLoginSuccess() throws Exception {
        UserTO user = UserTO.builder()
                .email("test1@gmail.com")
                .password("1234")
                .build();

        this.mockMvc.perform(post("/api/user/login")
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    @DisplayName("내 정보 조회")
    public void getUserSuccess() throws Exception {


        this.mockMvc.perform(get("/api/user")
                .header("authorization","eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjYsImV4cCI6MTYyODU4NDM3MX0.kOc_3M6mfu7tkxP8AKRMhbvNjNWdY-AnbLNbjBns0rQ")
                )
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }
}