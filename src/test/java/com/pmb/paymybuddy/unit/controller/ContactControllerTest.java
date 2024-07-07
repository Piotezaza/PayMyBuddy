package com.pmb.paymybuddy.unit.controller;

import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setEmail("test@test.com");
        when(userService.getUserByEmail(anyString())).thenReturn(user);
    }

    @Test
    @WithMockUser(username = "test@test.com")
    @DisplayName("Should return contact view with user model attribute")
    public void mainContactView() throws Exception {
        mockMvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "test@test.com")
    @DisplayName("Should delete contact and redirect to contact view with success message")
    public void deleteContact() throws Exception {
        when(userService.getUserByEmail(anyString())).thenReturn(user);
        when(userService.getUserById(1)).thenReturn(new User());
        mockMvc.perform(post("/contact/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/contact?successDelete"));
    }
}