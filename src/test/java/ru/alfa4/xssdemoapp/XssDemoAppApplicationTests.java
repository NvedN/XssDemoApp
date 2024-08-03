package ru.alfa4.xssdemoapp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class XssDemoAppApplicationTests {

  @Autowired private MockMvc mockMvc;

  @Test
  public void testContentSecurityPolicyHeader() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/login"))
        .andExpect(status().isOk())
        .andExpect(header().string("Content-Security-Policy", "default-src 'self'"));
  }

  @Test
  public void testXSSProtection() throws Exception {

    String xssPayload = "<script>alert('XSS')</script>";

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/login")
                .param("username", xssPayload)
                .param("password", "password"))
        .andExpect(status().is4xxClientError())
        .andExpect(
            content()
                .string(org.hamcrest.Matchers.not(org.hamcrest.Matchers.containsString("XSS"))));
  }
}
