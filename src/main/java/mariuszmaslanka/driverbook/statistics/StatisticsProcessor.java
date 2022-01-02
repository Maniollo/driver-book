package mariuszmaslanka.driverbook.statistics;

import lombok.RequiredArgsConstructor;
import mariuszmaslanka.driverbook.elastic.ElasticsearchStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StatisticsProcessor {

  private final ElasticsearchStorageService<StatisticsData> storageService;
  private final StatisticsRepository driverRepository;

  @Value("#{'${elasticsearch.index.name.statistics}'}")
  private String statisticsIndexName;

  public void refreshStatisticsData() {
    storageService.store(driverRepository.getAll(), statisticsIndexName);
  }
}
