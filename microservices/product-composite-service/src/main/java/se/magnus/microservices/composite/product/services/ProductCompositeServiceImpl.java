package se.magnus.microservices.composite.product.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.internal.Function;
import org.springframework.web.bind.annotation.RestController;

import se.magnus.api.composite.product.ProductAggregate;
import se.magnus.api.composite.product.ProductCompositeService;
import se.magnus.api.composite.product.RecommendationSummary;
import se.magnus.api.composite.product.ReviewSummary;
import se.magnus.api.composite.product.ServiceAddresses;
import se.magnus.api.core.product.Product;
import se.magnus.api.core.recommendation.Recommendation;
import se.magnus.api.core.review.Review;
import se.magnus.util.http.ServiceUtil;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {

  @Autowired private ServiceUtil serviceUtil;
  @Autowired private ProductCompositeIntegration integration;

  public ProductAggregate getProduct(int productId) {
    Product product = integration.getProduct(productId);
    List<Recommendation> recommendations = integration.getRecommendations(productId);
    List<Review> reviews = integration.getReviews(productId);
    return createProductAggregate(product, recommendations, reviews, serviceUtil.getServiceAddress());
  }

  private ProductAggregate createProductAggregate(
    Product product, 
    List<Recommendation> recommendations,
    List<Review> reviews, 
    String serviceAddress) {

    // Copy summary recommendation info, if available
    List<RecommendationSummary> recommendationSummaries = 
      (recommendations == null) ? null : recommendations.stream()
        .map(r -> new RecommendationSummary(r.getRecommendationId(), r.getAuthor(), r.getRate()))
        .toList();

    // Copy summary review info, if available
    List<ReviewSummary> reviewSummaries = 
      (reviews == null) ? null : reviews.stream()
        .map(r -> new ReviewSummary(r.getReviewId(), r.getAuthor(), r.getSubject()))
        .toList();

    // Create aggregate info regarding microservices Addresses
    String productAddress = product.getServiceAddress();
    String reviewAddress = getFirstPropertyOrElse(reviews, Review::getServiceAddress, "");
    String recommendationAddress = getFirstPropertyOrElse(recommendations, Recommendation::getServiceAddress, "");
    ServiceAddresses serviceAddresses = new ServiceAddresses(serviceAddress, productAddress, reviewAddress, recommendationAddress);
    
    return new ProductAggregate(
      product.getProductId(), product.getName(), product.getWeight(), recommendationSummaries, reviewSummaries, serviceAddresses);
  }

  private <T, R> R getFirstPropertyOrElse(List<T> list, Function<T, R> getterFn, R fallback) {
    if (list != null && list.size() > 0) {
      return getterFn.apply(list.get(0));
    } else {
      return fallback;
    }
  }
  
}
