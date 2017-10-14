package lab.zlren.security.demo.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void queryUserList() throws Exception {
        String content = mockMvc.perform(
                get("/user")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .param("username", "zlren"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3)).andReturn().getResponse().getContentAsString();

        log.info("{}", content);
    }

    @Test
    public void queryUserInfo() throws Exception {
        String content = mockMvc.perform(get("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("tom")).andReturn().getResponse().getContentAsString();

        log.info("{}", content);
    }

    @Test
    public void createUser() throws Exception {
        String content = "{\"username\":\"tom\",\"password\":null}";
        mockMvc.perform(post("/user").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void updateUser() throws Exception {
        Date date = new Date();
        String content = "{\"id\": \"1\", \"username\": \"tom\", \"password\":\"\", \"birthday\":" + date.getTime() +
                "}";
        String result = mockMvc.perform(put("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8).content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andReturn().getResponse().getContentAsString();

        log.info("{}", result);
    }


    @Test
    public void deleteUser() throws Exception {
        mockMvc.perform(delete("/user/1").contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk());
    }

    @Test
    public void upload() throws Exception {
        String result = mockMvc.perform(fileUpload("/file")
                .file(new MockMultipartFile("file", "test.txt", "multipart/form-data", "hello-upload".getBytes
                        ("UTF-8"))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        log.info(result);
    }
}