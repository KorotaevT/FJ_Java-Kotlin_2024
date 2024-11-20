package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.service.AuthService;
import org.example.service.CategoryService;
import org.example.service.EventService;
import org.example.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.web.WebAppConfiguration;

@SpringBootTest(classes = {SpringAppApplication.class})
@EnableAutoConfiguration
@AutoConfigureMockMvc
@WebAppConfiguration
public abstract class AbstractTestContainer {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected AuthService authService;

    @Autowired
    protected EventService eventService;

    @MockBean
    protected CategoryService categoryService;

    @MockBean
    protected LocationService locationService;

}