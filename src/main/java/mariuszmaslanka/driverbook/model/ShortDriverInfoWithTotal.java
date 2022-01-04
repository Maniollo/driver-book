package mariuszmaslanka.driverbook.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class ShortDriverInfoWithTotal {
  int count;
  List<ShortDriverInfo> drivers;
}
