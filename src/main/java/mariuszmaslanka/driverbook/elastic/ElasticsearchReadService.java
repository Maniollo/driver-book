package mariuszmaslanka.driverbook.elastic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
@Component
public class ElasticsearchReadService<T> {
  private final RestHighLevelClient client;
  private final ObjectMapper objectMapper;

  public List<T> read(String indexName, TypeReference<T> typeReference) {
    try {
      SearchRequest searchRequest = createSearchRequest(indexName);
      SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
      SearchHit[] hits = searchResponse.getHits().getHits();

      List<T> fetchedList = stream(hits)
          .map(it -> toOutputDocument(it, typeReference))
          .collect(toList());
      log.info("There were {} documents fetched from index '{}'", fetchedList.size(), indexName);
      return fetchedList;
    } catch (IOException e) {
      throw new ElasticsearchReadException(e);
    }
  }

  private SearchRequest createSearchRequest(String indexName) {
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    searchSourceBuilder.size(1000);

    SearchRequest searchRequest = new SearchRequest();
    searchRequest.indices(indexName);
    searchRequest.source(searchSourceBuilder);
    return searchRequest;
  }

  private T toOutputDocument(SearchHit hit, TypeReference<T> typeRef) {
    try {
      return objectMapper.readValue(hit.getSourceAsString(), typeRef);
    } catch (JsonProcessingException e) {
      throw new ElasticsearchReadException(e);
    }
  }
}
