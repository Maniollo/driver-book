package mariuszmaslanka.driverbook;

import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

class DriverBookElasticsearchContainer extends ElasticsearchContainer {

  private static final String ELASTICSEARCH_DOCKER_IMG = "docker.elastic.co/elasticsearch/elasticsearch";
  private static final String ELASTICSEARCH_IMG_TAG = "7.17.6";
  private static final int PORT = 9200;

  DriverBookElasticsearchContainer() {
    super(DockerImageName
        .parse(ELASTICSEARCH_DOCKER_IMG)
        .withTag(ELASTICSEARCH_IMG_TAG));
    this.addFixedExposedPort(PORT, PORT);
  }
}

