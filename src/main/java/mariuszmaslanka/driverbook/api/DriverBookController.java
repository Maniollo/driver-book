package mariuszmaslanka.driverbook.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mariuszmaslanka.driverbook.driver.DriverDataProcessor;
import mariuszmaslanka.driverbook.driver.DriverEnrichProcessor;
import mariuszmaslanka.driverbook.model.DriversData;
import mariuszmaslanka.driverbook.statistics.StatisticsProcessor;
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
  private final StatisticsProcessor statisticsProcessor;
  private final DriverEnrichProcessor driverEnrichProcessor;

  @GetMapping("/refresh-all")
  void refreshAll() {
    log.info("Refreshing all data started");
    refreshStatistics();
    refreshDrivers();
    log.info("Refreshing all data completed");
  }

  @GetMapping("/refresh-drivers")
  void refreshDrivers() {
    log.info("Refreshing drivers data started");
    driverEnrichProcessor.prepareEnrichment();
    driverDataProcessor.refreshDriverData();
    log.info("Refreshing drivers data completed");
  }

  @GetMapping("/refresh-statistics")
  void refreshStatistics() {
    log.info("Refreshing statistics data started");
    statisticsProcessor.refreshStatisticsData();
    log.info("Refreshing statistics data completed");
  }

  @GetMapping("/drivers")
  ResponseEntity<DriversData> getAll(@RequestParam(required = false) String nationality,
                                     @RequestParam(required = false) Boolean active,
                                     @RequestParam(required = false) Integer bornYearFrom,
                                     @RequestParam(required = false) Integer bornYearTo,
                                     @RequestParam(required = false) Boolean hasTitle,
                                     @RequestParam(required = false) Boolean hasWin
                                     ) {

    Filters filters = Filters.builder()
        .nationality(nationality)
        .active(active)
        .birthFrom(bornYearFrom)
        .birthTo(bornYearTo)
        .hasTitle(hasTitle)
        .hasWin(hasWin)
        .build();

    log.info("Fetch drivers data started");
    DriversData drivers = driverDataProcessor.getDriverData(filters);
    log.info("Fetch drivers data completed");
    return new ResponseEntity<>(drivers, OK);
  }
}
