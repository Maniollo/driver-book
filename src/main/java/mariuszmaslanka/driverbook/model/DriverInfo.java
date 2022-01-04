package mariuszmaslanka.driverbook.model;

import lombok.Builder;
import lombok.Value;
import mariuszmaslanka.driverbook.driver.DriverStatistic;

import java.time.LocalDate;

@Value
@Builder
public class DriverInfo {
  String driverId;
  String code;
  String givenName;
  String familyName;
  LocalDate dateOfBirth;
  String nationality;
  boolean active;
  Integer permanentNumber;
  DriverStatistic statistics;
}
