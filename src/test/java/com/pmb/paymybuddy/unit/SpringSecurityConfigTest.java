package com.pmb.paymybuddy.unit;

import com.pmb.paymybuddy.model.CompteBancaire;
import com.pmb.paymybuddy.model.ComptePMB;
import com.pmb.paymybuddy.model.User;
import com.pmb.paymybuddy.repository.CompteBancaireRepository;
import com.pmb.paymybuddy.repository.ComptePMBRepository;
import com.pmb.paymybuddy.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SpringSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ComptePMBRepository comptePMBRepository;

    @MockBean
    private CompteBancaireRepository compteBancaireRepository;

    @BeforeEach
    public void setup() {
        // Initialize the test user and associated objects
        ComptePMB comptePMB = new ComptePMB();
        comptePMB.setId(1);

        CompteBancaire compteBancaire = new CompteBancaire();
        compteBancaire.setId(1);

        User testUser = new User();
        testUser.setEmail("user");
        testUser.setPassword("password");
        testUser.setComptePMB(comptePMB);
        testUser.setCompteBancaire(compteBancaire);
        when(comptePMBRepository.save(comptePMB)).thenReturn(comptePMB);
        when(compteBancaireRepository.save(compteBancaire)).thenReturn(compteBancaire);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userRepository.findByEmail("user")).thenReturn(testUser);
    }

    @Test
    public void shouldAllowAccessToLoginWhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAllowAccessToRegistrationWhenUnauthenticated() throws Exception {
        mockMvc.perform(get("/registration"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void shouldAllowLogoutWhenAuthenticated() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is2xxSuccessful());
    }
}