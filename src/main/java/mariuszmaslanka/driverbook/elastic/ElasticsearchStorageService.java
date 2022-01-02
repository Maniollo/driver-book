package mariuszmaslanka.driverbook.elastic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions.Type.ADD;
import static org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest.AliasActions.Type.REMOVE;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.WAIT_UNTIL;
import static org.elasticsearch.client.RequestOptions.DEFAULT;

@Slf4j
@RequiredArgsConstructor
@Component
public class ElasticsearchStorageService<T> {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss");
  private static final int NO_OF_SHARDS = 1;
  private static final int NO_OF_REPLICAS = 1;
  private final RestHighLevelClient client;
  private final ObjectMapper objectMapper;

  public void store(List<T> data, String indexNameAsAlias) {
    store(data, indexNameAsAlias, null);
  }

  public void store(List<T> data, String indexNameAsAlias, String pipeline) {
    if (data.isEmpty()) {
      log.info("Nothing to store");
      return;
    }

    Stopwatch stopwatch = Stopwatch.createStarted();
    log.info("Storing {} records into elasticsearch index '{}' started", data.size(), indexNameAsAlias);

    try {
      String index = indexNameAsAlias + "-" + ZonedDateTime.now().format(DATE_FORMATTER);
      createIndex(index);
      storeInIndex(index, data, pipeline);
      assignAlias(indexNameAsAlias, index);
    } catch (Exception e) {
      throw new ElasticsearchStoreException(e);
    }

    stopwatch.stop();
    log.info("Storing data into elasticsearch index '{}' done in {} ms", indexNameAsAlias, stopwatch.elapsed(MILLISECONDS));
  }

  private void storeInIndex(String indexName, List<T> data, String pipeline) {
    try {
      BulkRequest bulkRequest = new BulkRequest().setRefreshPolicy(WAIT_UNTIL);
      data.forEach(it -> {
        Map<String, Object> source = objectMapper.convertValue(it, new TypeReference<>() {
        });
        bulkRequest.add(new IndexRequest(indexName).source(source).setPipeline(pipeline));
      });
      BulkResponse bulkResponse = client.bulk(bulkRequest, DEFAULT);
      if (bulkResponse.hasFailures()) {
        throw new ElasticsearchStoreException(bulkResponse.buildFailureMessage());
      }
    } catch (IOException e) {
      throw new ElasticsearchStoreException(e);
    }
  }

  private void createIndex(String indexName) throws IOException {
    GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);

    if (client.indices().exists(getIndexRequest, DEFAULT)) {
      log.info("Index '{}' already exist.", indexName);
      return;
    }

    CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName)
        .settings(Settings.builder()
            .put("index.number_of_shards", NO_OF_SHARDS)
            .put("index.number_of_replicas", NO_OF_REPLICAS)
            .build());
    client.indices().create(createIndexRequest, DEFAULT);
  }

  private void assignAlias(String indexNameAsAlias, String indexName) throws IOException {
    IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();

    unassignExistingIndices(indexNameAsAlias, indicesAliasesRequest);
    assignToNewIndex(indexNameAsAlias, indexName, indicesAliasesRequest);
    client.indices().updateAliases(indicesAliasesRequest, DEFAULT);
  }

  private void assignToNewIndex(String indexName, String index, IndicesAliasesRequest indicesAliasesRequest) {
    AliasActions aliasActions = new AliasActions(ADD).index(index).alias(indexName);
    indicesAliasesRequest.addAliasAction(aliasActions);
  }

  private void unassignExistingIndices(String indexName, IndicesAliasesRequest indicesAliasesRequest) throws IOException {
    String[] assignedIndicesUnderAlias = client.indices().getAlias(new GetAliasesRequest(indexName), DEFAULT)
        .getAliases().keySet().toArray(String[]::new);

    if (assignedIndicesUnderAlias.length > 0) {
      AliasActions alias = new AliasActions(REMOVE).indices(assignedIndicesUnderAlias).alias(indexName);
      indicesAliasesRequest.addAliasAction(alias);
    }
  }
}
