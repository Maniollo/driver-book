package mariuszmaslanka.driverbook.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class NationalityInfo {
  int count;
  int wins;
  int titles;
  double avgRaces;
  List<ShortDriverInfo> drivers;
}
