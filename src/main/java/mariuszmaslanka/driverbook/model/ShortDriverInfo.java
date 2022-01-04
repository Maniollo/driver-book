package mariuszmaslanka.driverbook.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShortDriverInfo {
  String driverId;
  String givenName;
  String familyName;
}
