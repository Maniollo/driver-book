package mariuszmaslanka.driverbook;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
class DriverBookApplicationIT {

  private static final String EXPECTED_POLISH_DRIVERS = "expected/polish_drivers.json";
  private static final String EXPECTED_BRITISH_CHAMPS_BORN_IN_80_S = "expected/british_champs_born_in_80s.json";
  @Autowired
  private MockMvc mvc;

  @Container
  private static final ElasticsearchContainer elasticsearchContainer = new DriverBookElasticsearchContainer();

  @BeforeAll
  static void setUp() {
    elasticsearchContainer.start();
  }

  @BeforeEach
  void testIsContainerRunning() {
    assertTrue(elasticsearchContainer.isRunning());
  }

  @Test
  void shouldReturnPolishDrivers() throws Exception {
//    given
    mvc.perform(get("/api/refresh-all"))
        .andExpect(status().isOk());

//    when
    MvcResult result = mvc.perform(get("/api/drivers?nationality=Polish"))
        .andExpect(status().isOk())
        .andReturn();

//    then
    assertEquals(readFrom(EXPECTED_POLISH_DRIVERS), result.getResponse().getContentAsString());
  }

  @Test
  void shouldReturnBritishChampsBornInIn80s() throws Exception {
//    given
    mvc.perform(get("/api/refresh-all"))
        .andExpect(status().isOk());

//    when
    MvcResult result = mvc.perform(get("/api/drivers?nationality=British&bornYearFrom=1980&bornYearTo=1989&hasTitle=true"))
        .andExpect(status().isOk())
        .andReturn();

//    then
    assertEquals(readFrom(EXPECTED_BRITISH_CHAMPS_BORN_IN_80_S), result.getResponse().getContentAsString());
  }

  private String readFrom(String fileName) {
    try {
      return new String(getClass().getClassLoader().getResourceAsStream(fileName).readAllBytes()).replaceAll("\\s+", "");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @AfterAll
  static void afterAll() {
    elasticsearchContainer.stop();
  }

}
