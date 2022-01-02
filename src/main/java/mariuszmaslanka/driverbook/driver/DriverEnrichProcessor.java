package mariuszmaslanka.driverbook.driver;

import lombok.RequiredArgsConstructor;
import mariuszmaslanka.driverbook.elastic.ElasticsearchEnrichProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class DriverEnrichProcessor {

  private final ElasticsearchEnrichProcessor elasticsearchEnrichProcessor;

  @Value("#{'${elasticsearch.policy.name}'}")
  private String policyName;

  @Value("#{'${elasticsearch.pipeline.name}'}")
  private String pipelineName;

  @Value("#{'${elasticsearch.pipeline.path}'}")
  private String pipelinePath;

  @Value("#{'${elasticsearch.index.name.statistics}'}")
  private String sourceIndex;

  public void prepareEnrichment() {
    elasticsearchEnrichProcessor.preparePolicy(policyName, sourceIndex, "driverId", List.of("races", "wins", "titles"));
    elasticsearchEnrichProcessor.preparePipeline(pipelineName, pipelinePath);
  }
}
