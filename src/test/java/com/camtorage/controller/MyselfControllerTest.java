package com.camtorage.controller;

import com.camtorage.db.gear.service.GearService;
import com.camtorage.db.geartype.repository.GearTypeRepository;
import com.camtorage.db.user.UserService;
import com.camtorage.entity.gear.GearRequest;
import com.camtorage.entity.geartype.GearType;
import com.camtorage.entity.user.UserRequest;
import com.camtorage.entity.user.UserToken;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MyselfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GearService gearService;

    @Autowired
    private UserService userService;

    @Autowired
    private GearTypeRepository gearTypeRepository;

    private UserToken userToken;


    @BeforeEach
    public void init() {

        GearType gearType = new GearType();
        gearType.setId(1);
        gearType.setName("의자");
        gearType.setSeq(1);
        gearTypeRepository.save(gearType);

        String email = "test3333@gmail.com";
        String password = "1234";
        UserRequest user = UserRequest.builder()
                .email(email)
                .name("tester")
                .password(password)
                .build();
        userService.saveUser(user);

        this.userToken = userService.loginUser(email, password);

    }

    @Test
    @DisplayName("save_gear_success")
    public void 장비_저장_성공() throws Exception {
        GearRequest gearRequest = createGearRequest(1);

        mockMvc.perform(post("/api/myself/gear")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("authorization", userToken.getToken())
                .param("name", gearRequest.getName())
                .param("capacity", gearRequest.getCapacity())
                .param("buyDt", gearRequest.getBuyDt())
                .param("company", gearRequest.getCompany())
                .param("color", gearRequest.getColor())
                .param("gearTypeId", String.valueOf(gearRequest.getGearTypeId()))
                .param("price", String.valueOf(gearRequest.getPrice())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("name").value(gearRequest.getName()))
        ;
    }

    @Test
    @DisplayName("장비 조회")
    public void getListGear() throws Exception {
        GearRequest gearRequest = createGearRequest(1);

        mockMvc.perform(get("/api/myself/gear")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("authorization", userToken.getToken()))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    public GearRequest createGearRequest(Integer index) {
        return GearRequest.builder()
                .name("gearTest" + index)
                .price(1000)
                .company("company" + index)
                .gearTypeId(1)
                .color("blue")
                .capacity("4")
                .buyDt("2021-09-22")
                .build();
    }

}