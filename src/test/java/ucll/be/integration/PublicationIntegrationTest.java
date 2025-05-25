package ucll.be.integration;

import java.net.URL;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import ucll.be.controller.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ucll.be.model.Publication;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.util.AssertionErrors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@AutoConfigureMockMvc
public class PublicationIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testResourceExists(){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("test-data.sql");
        assertNotNull("test-data.sql resource should exist", resource);
    }

    @Test
    public void shouldReturnAllPublication() throws Exception{
        var result = mockMvc.perform(get("/publications"))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        List<?> publication = objectMapper.readValue(json, new TypeReference<>() {});
        assertThat(publication).isEmpty();
    }

    @Test
    public void shouldFilterPublicationsByTitleAndType() throws Exception{
        var result = mockMvc.perform(get("/publications")
                        .param("title", "Jungle Book")
                        .param("type", "Book"))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        List<?>publication = objectMapper.readValue(json, new TypeReference<>() {});
        assertThat(publication).isNotEmpty();
    }

    @Test
    public void shouldReturnPublicationWithEnoughStock() throws Exception{
        var result = mockMvc.perform(get("/publications/stock/1"))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        List<?>publication = objectMapper.readValue(json, new TypeReference<>(){});
        assertThat(publication).isNotNull();
    }

    @Test
    public void shouldReturnBadRequestWhenStockNegative() throws Exception{
        mockMvc.perform(get("/publications/stock/-5"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldHandleStockEndPointWithOptionalNumber() throws Exception{
        var result = mockMvc.perform(get("/publications/stock/2"))
                .andExpect(status().isOk()).andReturn();

        String json = result.getResponse().getContentAsString();
        List<Publication> publication = objectMapper.readValue(json, new TypeReference<List<Publication>>(){});
        assertThat(publication).isNotNull();
    }
}
