package javamaven.javamaven.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserController userControllerMock;

    @Test
    void canGetAllUsers() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/api/user");
        MvcResult result = mvc.perform(request).andReturn();

//        System.out.println(result.getResponse().getContentAsString());
        assertThat(result.getResponse().getContentAsString()).isEqualTo("[]");
    }

    @Test
    @Disabled
    void canGetUser() throws Exception {
        // Need to work on this
        RequestBuilder request = MockMvcRequestBuilders.get("/api/user/search?id={id}", 1L);
        MvcResult result = mvc.perform(request).andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Disabled
    void getUser() {
    }

    @Test
    @Disabled
    void userSignUp() {
    }

    @Test
    @Disabled
    void userSignIn() {
    }

    @Test
    @Disabled
    void changePassword() {
    }
}