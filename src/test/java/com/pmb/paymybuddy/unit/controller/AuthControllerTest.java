package com.pmb.paymybuddy.unit.controller;

import com.pmb.paymybuddy.controller.AuthController;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(controllers = {AuthController.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("Should return login view when login endpoint is hit")
    void shouldReturnLoginView() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    @DisplayName("Should return registration view with empty user when registration endpoint is hit")
    void shouldReturnRegistrationViewWithEmptyUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("registration"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    @DisplayName("Should redirect to login with success message when valid user is registered")
    void shouldRedirectToLoginWithSuccessMessageWhenValidUserIsRegistered() throws Exception {
        User user = new User();
        doNothing().when(userService).addUser(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login?success"));
    }

    @Test
    @DisplayName("Should return registration view with error message when exception is thrown during registration")
    void shouldReturnRegistrationViewWithErrorMessageWhenExceptionIsThrownDuringRegistration() throws Exception {
        User user = new User();
        doThrow(new RuntimeException("Test exception")).when(userService).addUser(any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/registration")
                        .flashAttr("user", user))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("registration"))
                .andExpect(MockMvcResultMatchers.model().attribute("message", "Test exception"));
    }
}