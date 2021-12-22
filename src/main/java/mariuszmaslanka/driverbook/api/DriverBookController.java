package mariuszmaslanka.driverbook.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mariuszmaslanka.driverbook.driver.DriverData;
import mariuszmaslanka.driverbook.driver.DriverDataProcessor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api")
@Slf4j
@RequiredArgsConstructor
public class DriverBookController {
  private final DriverDataProcessor driverDataProcessor;

  @GetMapping("/refresh")
  void refresh() {
    log.info("Refreshing data started");
    driverDataProcessor.refreshDriverData();
    log.info("Refreshing data completed");
  }

  @GetMapping("/drivers")
  ResponseEntity<List<DriverData>> getAll() {
    log.info("Fetch drivers data started");
    List<DriverData> drivers = driverDataProcessor.getDriverData();
    log.info("Fetch drivers data completed");
    return new ResponseEntity<>(drivers, OK);
  }
}
