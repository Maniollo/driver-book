package mariuszmaslanka.driverbook.api;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Filters {
  String nationality;
  Boolean active;
  Integer birthFrom;
  Integer birthTo;
}
