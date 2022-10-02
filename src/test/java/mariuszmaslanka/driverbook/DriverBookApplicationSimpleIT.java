package mariuszmaslanka.driverbook;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Testcontainers
class DriverBookApplicationSimpleIT {
  @Container
  private static final ElasticsearchContainer elasticsearchContainer = new DriverBookElasticsearchContainer();

  @BeforeAll
  static void setUp() {
    elasticsearchContainer.start();
  }

  @Test
  void shouldContainerRun() {
    assertTrue(elasticsearchContainer.isRunning());
  }

  @AfterAll
  static void afterAll() {
    elasticsearchContainer.stop();
  }
}
