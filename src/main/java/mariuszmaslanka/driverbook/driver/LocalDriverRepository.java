package mariuszmaslanka.driverbook.driver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Component
class LocalDriverRepository implements DriverRepository {

  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
  private final Resource resourceFile;
  private final ObjectMapper objectMapper;

  LocalDriverRepository(@Value("classpath:data/drivers.json") Resource resourceFile,
                        ObjectMapper objectMapper) {
    this.resourceFile = resourceFile;
    this.objectMapper = objectMapper;
  }

  @Override
  public List<Driver> getAll() {
    try {
      objectMapper.setDateFormat(DATE_FORMAT);
      return objectMapper.readValue(resourceFile.getFile(), new TypeReference<>() {
      });
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
