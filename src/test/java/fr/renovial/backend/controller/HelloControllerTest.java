package fr.renovial.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
class HelloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void sayHello_withAuthentification() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, World!"));
    }

    @Test
    void sayHello_withoutAuthentification() throws Exception {
        mockMvc.perform(get("/hello"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(authorities = {"ROLE_ADMIN"})
    void sayHelloAdmin_withAuthentificationAdmin() throws Exception {
        mockMvc.perform(get("/admin/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, Admin!"));
    }

    @Test
    @WithMockUser(authorities = {"ROLE_USER"})
    void sayHelloUser_withAuthentificationUser() throws Exception {
        mockMvc.perform(get("/admin/hello"))
                .andExpect(status().isForbidden());
    }

    @Test
    void sayHelloAdmin_withoutAuthentification() throws Exception {
        mockMvc.perform(get("/admin/hello"))
                .andExpect(status().isForbidden());
    }
}