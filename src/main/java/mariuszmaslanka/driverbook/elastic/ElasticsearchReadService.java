package mariuszmaslanka.driverbook.elastic;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static org.elasticsearch.client.RequestOptions.DEFAULT;

@RequiredArgsConstructor
@Slf4j
@Component
public class ElasticsearchReadService {
  private static final int MAX_NO_OF_DOCUMENTS = 1000;
  private final RestHighLevelClient client;

  public SearchResponse read(String indexName, QueryBuilder query, List<AggregationBuilder> aggregations) {
    try {
      SearchRequest searchRequest = createSearchRequest(indexName, query, aggregations);
      return client.search(searchRequest, DEFAULT);
    } catch (IOException e) {
      throw new ElasticsearchReadException(e);
    }
  }

  private SearchRequest createSearchRequest(String indexName, QueryBuilder query, List<AggregationBuilder> aggregations) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.size(MAX_NO_OF_DOCUMENTS);
    searchSourceBuilder.query(query);
    for (AggregationBuilder aggregation : aggregations) {
      searchSourceBuilder.aggregation(aggregation);
    }

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices(indexName);
    searchRequest.source(searchSourceBuilder);
    return searchRequest;
  }
}
