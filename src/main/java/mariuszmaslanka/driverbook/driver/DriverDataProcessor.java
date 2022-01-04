package mariuszmaslanka.driverbook.driver;

import lombok.RequiredArgsConstructor;
import mariuszmaslanka.driverbook.api.Filters;
import mariuszmaslanka.driverbook.elastic.ElasticsearchReadService;
import mariuszmaslanka.driverbook.elastic.ElasticsearchStorageService;
import mariuszmaslanka.driverbook.model.DriversData;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DriverDataProcessor {

  private final ElasticsearchStorageService<Driver> storageService;
  private final ElasticsearchReadService searchService;
  private final DriverDataQueryProvider driverDataQueryProvider;
  private final DriverRepository driverRepository;
  private final DriverAggregationProvider driverAggregationProvider;
  private final DriverAggregationResultMapper driverAggregationResultMapper;

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
    SearchResponse searchResponse = searchService.read(driversEnrichedIndexName,
        driverDataQueryProvider.getQuery(filters),
        driverAggregationProvider.getAggregations());

    return driverAggregationResultMapper.map(searchResponse);
  }
}
