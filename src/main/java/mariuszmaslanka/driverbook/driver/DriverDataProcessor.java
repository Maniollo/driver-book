package mariuszmaslanka.driverbook.driver;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
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
  private final DriverRepository driverRepository;

  @Value("#{'${elasticsearch.index.name.drivers}'}")
  private String driversRawIndexName;

  public void refreshDriverData() {
    storageService.store(driverRepository.getAll(), driversRawIndexName);
  }

  public List<DriverData> getDriverData() {
    return searchService.read(driversRawIndexName, new TypeReference<>() {
    });
  }
}
