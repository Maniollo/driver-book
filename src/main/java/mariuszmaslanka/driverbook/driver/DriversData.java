package mariuszmaslanka.driverbook.driver;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DriversData {
  int count;
  List<DriverData> drivers;
}
