package mariuszmaslanka.driverbook.driver;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
public class DriverData {
  String driverId;
  String code;
  String givenName;
  String familyName;
  LocalDate dateOfBirth;
  String nationality;
  boolean active;
  Integer permanentNumber;
}
