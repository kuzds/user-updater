package com.example.user.updater.controller;

import com.example.user.updater.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.user.updater.controller.UserControllerAdvice.INTERNAL_SERVER_ERROR;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        properties = {"spring.main.lazy-initialization=true"})
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    void payment_noOrder() throws Exception {
        Long departmentId = 1L;
        Mockito.doThrow(new RuntimeException())
                .when(userService).updateUsersByDepartmentId(departmentId);

        mockMvc.perform(post("/users/update")
                        .param("departmentId", String.valueOf(departmentId))
                        .contentType("application/json")
                )
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(INTERNAL_SERVER_ERROR));
    }
}