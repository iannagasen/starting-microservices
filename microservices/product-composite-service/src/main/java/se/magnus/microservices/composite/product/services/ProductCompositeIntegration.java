package se.magnus.microservices.composite.product.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.magnus.api.core.product.Product;
import se.magnus.api.core.product.ProductService;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.recommendation.RecommendationService;
import se.magnus.api.core.review.Review;
import se.magnus.api.core.review.ReviewService;
import se.magnus.api.exceptions.InvalidInputException;
import se.magnus.api.exceptions.NotFoundException;
import se.magnus.microservices.composite.product.config.ExternalServiceConfig;
import se.magnus.util.http.HttpErrorInfo;

@Component
public class ProductCompositeIntegration 
  implements ProductService, 
             RecommendationService, 
             ReviewService {

  private static final Logger LOG = LoggerFactory.getLogger(ProductCompositeIntegration.class);

  @Autowired private RestTemplate restTemplate;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private ExternalServiceConfig externalConfig;


  public Product getProduct(int productId) {

    try {
      String url = externalConfig.getProductServiceUrl() + productId;

      LOG.debug("Will call getProduct API on URL: {}", url);
      Product product = restTemplate.getForObject(url, Product.class);

      LOG.debug("Found a product with id: {}", product.getProductId());
      return product;

    } catch (HttpClientErrorException ex) {
      switch (HttpStatus.resolve(ex.getStatusCode().value())) {
        case NOT_FOUND: 
          throw new NotFoundException(getErrorMessage(ex));
        case UNPROCESSABLE_ENTITY:
          throw new InvalidInputException(getErrorMessage(ex));
        default:
          LOG.warn("Got an unexpected HTTP error: {}, will rethrow it", ex.getStatusCode());
          LOG.warn("Error body: {}", ex.getResponseBodyAsString());
          throw ex;
      }
    }
  }


  public List<Review> getReviews(int productId) {

    try {
      String url = externalConfig.getReviewServiceUrl() + productId;

      LOG.debug("Will call getReviews API on URL: {}", url);
      List<Review> reviews = restTemplate
        .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Review>>() {})
        .getBody(); 

      LOG.debug("Found {} reviews for a product with id: {}", reviews.size(), productId);
      return reviews;

    } catch (Exception e) {
      LOG.warn("Got an exception while requesting reviews, return zero reviews: {}", e.getMessage());
      return new ArrayList<>();
    }
  }

  @Override
  public List<Recommendation> getRecommendations(int productId) {
    
    try {
      String url = externalConfig.getRecommendationServiceUrl() + productId;

      LOG.debug("Will call getRecommendations API on URL: {}", url);
      List<Recommendation> recommendations = restTemplate
        .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Recommendation>>() {})
        .getBody();
    
      LOG.debug("Found {} recommendations for a product with id: {}", recommendations.size(), productId);
      return recommendations;

    } catch (Exception e) {
      LOG.warn("Got an exception while requesting recommendations, return zero recommendations: {}", e.getMessage());
      return new ArrayList<>();
    }
  }

  private String getErrorMessage(HttpClientErrorException ex) {
    try {
      return objectMapper.readValue(ex.getResponseBodyAsString(), HttpErrorInfo.class).getMessage();
    } catch (IOException ioex) {
      return ex.getMessage();
    }
  }

}
