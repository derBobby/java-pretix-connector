package eu.planlos.javapretixconnector.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static eu.planlos.javapretixconnector.PretixTestDataUtility.orderApprovedHook;
import static eu.planlos.javapretixconnector.controller.PretixWebhookController.URL_WEBHOOK;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tutorial for a lot of the functions here
 * https://www.baeldung.com/integration-testing-in-spring
 */
@SpringBootTest
@WebAppConfiguration
class PretixWebhookControllerIT {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void givenWac_providesController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertTrue(webApplicationContext.containsBean("pretixWebhookController"));
    }

    @Test
    public void orderApprovedHook_createsAccount() throws Exception {
        String hookJson = mapper.writeValueAsString(orderApprovedHook());
        this.mockMvc.perform(
                        post(URL_WEBHOOK)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(hookJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    /*
     * Demo code from:
     * https://www.baeldung.com/integration-testing-in-spring
     */
    @Disabled
    @Test
    public void givenHomePageURI_whenMockMVC_thenReturnsIndexJSPViewName() throws Exception {
        this.mockMvc.perform(get("/homePage")).andDo(print())
                .andExpect(view().name("index"));
    }

    @Disabled
    @Test
    public void givenGreetURI_whenMockMVC_thenVerifyResponse() throws Exception {
        MvcResult mvcResult = this.mockMvc
                .perform(get("/greet"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello World!!!"))
                .andReturn();

        assertEquals("application/json;charset=UTF-8",
                mvcResult.getResponse().getContentType());
    }

    @Disabled
    @Test
    public void givenGreetURIWithPathVariable_whenMockMVC_thenResponseOK() throws Exception {
        this.mockMvc
                .perform(get("/greetWithPathVariable/{name}", "John"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.message").value("Hello World John!!!"));
    }

    @Disabled
    @Test
    public void givenGreetURIWithQueryParameter_whenMockMVC_thenResponseOK() throws Exception {
        this.mockMvc
                .perform(get("/greetWithQueryVariable").param("name", "John Doe"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.message").value("Hello World John Doe!!!"));
    }

    @Disabled
    @Test
    public void givenGreetURIWithPost_whenMockMVC_thenVerifyResponse() throws Exception {
        this.mockMvc
                .perform(post("/greetWithPost")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.message").value("Hello World!!!"));
    }
}