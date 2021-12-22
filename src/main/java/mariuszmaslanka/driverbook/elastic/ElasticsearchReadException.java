package mariuszmaslanka.driverbook.elastic;

public class ElasticsearchReadException extends RuntimeException {
  public ElasticsearchReadException(Exception e) {
    super(e);
  }
}
