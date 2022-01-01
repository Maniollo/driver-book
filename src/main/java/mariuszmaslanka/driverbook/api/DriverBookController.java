package mariuszmaslanka.driverbook.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mariuszmaslanka.driverbook.driver.DriverDataProcessor;
import mariuszmaslanka.driverbook.driver.DriversData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
  ResponseEntity<DriversData> getAll(@RequestParam(required = false) String nationality,
                                     @RequestParam(required = false) Boolean active,
                                     @RequestParam(required = false) Integer bornYearFrom,
                                     @RequestParam(required = false) Integer bornYearTo) {

    Filters filters = Filters.builder()
        .nationality(nationality)
        .active(active)
        .birthFrom(bornYearFrom)
        .birthTo(bornYearTo)
        .build();

    log.info("Fetch drivers data started");
    DriversData drivers = driverDataProcessor.getDriverData(filters);
    log.info("Fetch drivers data completed");
    return new ResponseEntity<>(drivers, OK);
  }
}
