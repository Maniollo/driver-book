package mariuszmaslanka.driverbook.statistics;

import java.util.List;

interface StatisticsRepository {
  List<StatisticsData> getAll();
}
