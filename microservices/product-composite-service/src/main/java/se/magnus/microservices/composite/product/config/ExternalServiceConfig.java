package se.magnus.microservices.composite.product.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="app")
public class ExternalServiceConfig {
  
  private ExternalServiceProperties productService;
  private ExternalServiceProperties recommendationService;
  private ExternalServiceProperties reviewService;

  public String getProductServiceUrl() {
    return "http://%s:%s/product/".formatted(productService.getHost(), productService.getPort());
  }

  public String getRecommendationServiceUrl() {
    return "http://%s:%s/recommendation?".formatted(recommendationService.getHost(), recommendationService.getPort());
  }

  public String getReviewServiceUrl() {
    return "http://%s:%s/review?productId=".formatted(reviewService.getHost(), reviewService.getPort());
  }
  
  public ExternalServiceProperties getProductService() { return productService; }
  public void setProductService(ExternalServiceProperties productService) { this.productService = productService; }
  
  public ExternalServiceProperties getRecommendationService() { return recommendationService; }
  public void setRecommendationService(ExternalServiceProperties recommendationService) { this.recommendationService = recommendationService; }
  
  public ExternalServiceProperties getReviewService() { return reviewService; }
  public void setReviewService(ExternalServiceProperties reviewService) { this.reviewService = reviewService; }


  static class ExternalServiceProperties {
    private String host;
    private int port;

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
  }

  // static class ProductService {
  //   private String host;
  //   private int port;

  //   public String getHost() { return host; }
  //   public void setHost(String host) { this.host = host; }

  //   public int getPort() { return port; }
  //   public void setPort(int port) { this.port = port; }
  // }

  // static class RecommendationService {
  //   private String host;
  //   private int port;

  //   public String getHost() { return host; }
  //   public void setHost(String host) { this.host = host; }

  //   public int getPort() { return port; }
  //   public void setPort(int port) { this.port = port; }
  // }

  // static class ReviewService {
  //   private String host;
  //   private int port;

  //   public String getHost() { return host; }
  //   public void setHost(String host) { this.host = host; }

  //   public int getPort() { return port; }
  //   public void setPort(int port) { this.port = port; }
  // }
}
