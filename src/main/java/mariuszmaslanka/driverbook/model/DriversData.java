package mariuszmaslanka.driverbook.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@Builder
public class DriversData {
  int count;
  List<DriverInfo> drivers;
  Map<String, NationalityInfo> byNationality;
  Map<String, ShortDriverInfoWithTotal> byTitles;
  Map<String, ShortDriverInfoWithTotal> byActive;
}
