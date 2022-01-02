package mariuszmaslanka.driverbook.driver;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import mariuszmaslanka.driverbook.api.Filters;
import mariuszmaslanka.driverbook.elastic.ElasticsearchReadService;
import mariuszmaslanka.driverbook.elastic.ElasticsearchStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DriverDataProcessor {

  private final ElasticsearchStorageService<Driver> storageService;
  private final ElasticsearchReadService<DriverData> searchService;
  private final DriverDataQueryProvider driverDataQueryProvider;
  private final DriverRepository driverRepository;

  @Value("#{'${elasticsearch.index.name.drivers}'}")
  private String driversRawIndexName;

  @Value("#{'${elasticsearch.index.name.enriched-drivers}'}")
  private String driversEnrichedIndexName;

  @Value("#{'${elasticsearch.pipeline.name}'}")
  private String pipelineName;

  public void refreshDriverData() {
    storageService.store(driverRepository.getAll(), driversRawIndexName);
    storageService.store(driverRepository.getAll(), driversEnrichedIndexName, pipelineName);
  }

  public DriversData getDriverData(Filters filters) {
    List<DriverData> driverDataList = searchService.read(driversEnrichedIndexName,
        driverDataQueryProvider.getQuery(filters),
        new TypeReference<>() {
        });

    return DriversData.builder()
        .count(driverDataList.size())
        .drivers(driverDataList)
        .build();
  }
}
