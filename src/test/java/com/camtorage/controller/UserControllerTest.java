package com.camtorage.controller;

import com.camtorage.controller.dto.UserRequestDto;
import com.camtorage.db.user.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class UserControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    protected ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        String email = "test0@gmail.com";
        String password = "1234";
        String name = "tester";

        UserRequestDto.CreateRequest userRequest = new UserRequestDto.CreateRequest();
        userRequest.setEmail(email);
        userRequest.setName(name);
        userRequest.setPassword(password);

        userService.createUser(userRequest.toCommand());
    }

    @Test
    public void 회원가입이_성공적으로_수행된다() throws Exception {
        UserRequestDto.CreateRequest userRequest = new UserRequestDto.CreateRequest();
        userRequest.setName("tester");
        userRequest.setEmail("test1@gmail.com");
        userRequest.setPassword("1234");

        this.mockMvc.perform(post("/api/user")
            .param("email", userRequest.getEmail())
            .param("password", userRequest.getPassword())
            .param("name", userRequest.getName()))
            .andDo(print())
            .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName("회원가입 시 잘못된 파라메터를 넘길 경우")
    public void joinUserInvalidParameterTest() throws Exception {
        UserRequestDto.CreateRequest userRequest = new UserRequestDto.CreateRequest();
        userRequest.setName("tester");
        userRequest.setEmail("test1@gmail.com");
        userRequest.setPassword("1234");

        this.mockMvc.perform(post("/api/user")
                .param("email", "")
                .param("password", userRequest.getPassword()))
            .andDo(print())
            .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("회원 로그인 성공")
    public void userLoginSuccess() throws Exception {
        String email = "test0@gmail.com";
        String passwrd = "1234";

        this.mockMvc.perform(post("/api/user/login")
                .param("email", email)
                .param("password", passwrd)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("token").isNotEmpty())
        ;
    }

    @Test
    @DisplayName("내 정보 조회")
    public void getUserSuccess() throws Exception {
        String email = "test0@gmail.com";
        String password = "1234";

        String responseBody = this.mockMvc.perform(post("/api/user/login")
                .param("email", email)
                .param("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("token").isNotEmpty())
            .andReturn().getResponse().getContentAsString();
        ;
        Map<String, String> map = objectMapper.readValue(responseBody, Map.class);

        this.mockMvc.perform(get("/api/myself")
                .header("authorization", map.get("token"))
            )
            .andDo(print())
            .andExpect(status().isOk())
        ;
    }
}