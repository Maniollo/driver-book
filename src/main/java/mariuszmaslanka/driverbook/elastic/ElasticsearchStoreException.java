package mariuszmaslanka.driverbook.elastic;

public class ElasticsearchStoreException extends RuntimeException{
  public ElasticsearchStoreException(Exception e) {
    super(e);
  }

  public ElasticsearchStoreException(String msg) {
    super(msg);
  }
}
