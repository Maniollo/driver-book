package mariuszmaslanka.driverbook.elastic;

import com.google.common.io.ByteStreams;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.client.EnrichClient;
import org.elasticsearch.client.IngestClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.enrich.ExecutePolicyRequest;
import org.elasticsearch.client.enrich.GetPolicyRequest;
import org.elasticsearch.client.enrich.GetPolicyResponse;
import org.elasticsearch.client.enrich.PutPolicyRequest;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ElasticsearchEnrichProcessor {

  private final RestHighLevelClient client;

  public void preparePolicy(String policyName, String sourceIndex, String matchField, List<String> enrichedFields) {
    EnrichClient enrichClient = client.enrich();

    try {
      GetPolicyResponse policy = enrichClient.getPolicy(new GetPolicyRequest(policyName), RequestOptions.DEFAULT);

      if (policy.getPolicies().isEmpty()) {
        PutPolicyRequest policyRequest = new PutPolicyRequest(policyName, "match",
            List.of(sourceIndex),
            matchField,
            enrichedFields);
        enrichClient.putPolicy(policyRequest, RequestOptions.DEFAULT);
      }
      enrichClient.executePolicy(new ExecutePolicyRequest(policyName), RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("Error during creating enrichment policy: {}", e.getMessage(), e);
    }
  }

  public void preparePipeline(String pipelineName, String pipelinePath) {
    IngestClient ingestClient = client.ingest();

    try {
      InputStream inputStream = new ClassPathResource(pipelinePath).getInputStream();
      byte[] bytes = ByteStreams.toByteArray(inputStream);
      PutPipelineRequest putPipelineRequest = new PutPipelineRequest(pipelineName, new BytesArray(bytes), XContentType.JSON);
      ingestClient.putPipeline(putPipelineRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error("Error during creating pipeline: {}", e.getMessage(), e);
    }
  }
}
