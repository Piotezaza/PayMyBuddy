package com.pmb.paymybuddy.integration.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should return login view when login endpoint is hit")
    public void testLoginPageShouldBeDisplayed() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("Should authenticate user when login with correct credentials")
    public void testUserLoginWithSuccess() throws Exception {
        mockMvc.perform(formLogin("/login")
                        .user("piotezaza@live.fr")
                        .password("test"))
                .andExpect(authenticated());
    }

    @Test
    @DisplayName("Should not authenticate user when login with incorrect credentials")
    public void testUserLoginFail() throws Exception {
        mockMvc.perform(formLogin("/login")
                        .user("unknownuser@email.com")
                        .password("unknown"))
                .andExpect(unauthenticated());
    }
}
