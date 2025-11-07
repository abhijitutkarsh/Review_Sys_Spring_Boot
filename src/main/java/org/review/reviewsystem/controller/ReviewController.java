package org.review.reviewsystem.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.review.reviewsystem.model.Review;
import org.review.reviewsystem.model.ReviewToken;
import org.review.reviewsystem.service.CustomerService;
import org.review.reviewsystem.service.EmailService;
import org.review.reviewsystem.service.ProductService;
import org.review.reviewsystem.service.ReviewService;
import org.review.reviewsystem.service.ReviewTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewTokenService reviewTokenService;

    private final String securityKey = "bXJgYs7t+RylGceRP5hqv0FbCqRW4Ej4HqRm3f1FPZ4="; // Ideally from configuration or env

    @GetMapping
    public List<Review> getReviews() {
        return reviewService.getReviews();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable String id) {
        Optional<Review> review = reviewService.getReviewById(id);

        return review.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Review createReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review reviewDetails) {
        Optional<Review> review = reviewService.getReviewById(id);
        if (review.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Review existingReview = review.get();
        existingReview.setProductId(reviewDetails.getProductId());
        existingReview.setUserId(reviewDetails.getUserId());
        existingReview.setComment(reviewDetails.getComment());
        existingReview.setRating(reviewDetails.getRating());
        existingReview.setCreatedAt(reviewDetails.getCreatedAt());
        Review updatedReview = reviewService.saveReview(existingReview);
        return ResponseEntity.ok(updatedReview);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-review-request")
    public ResponseEntity<String> sendReviewRequest(@RequestParam String customerId, @RequestParam String productId) {
        var customer = customerService.getVerifiedCustomerById(customerId);
        var product = productService.getProductById(productId);

        if (customer == null || !customer.isVerified()) {
            return ResponseEntity.badRequest().body("Customer email not verified or customer not found.");
        }

        // Generate JWT Token with 30 days expiration
        String token = Jwts.builder()
                .setSubject(customer.getEmail())
                .claim("customerId", customerId)
                .claim("productId", productId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, securityKey)
                .compact();

        // Prepare product DTO for token storage
//        ProductDTO productDTO = new ProductDTO();
//        productDTO.setProductId(productId);
//        productDTO.setProductName(product.getTitle());

        // Create ReviewToken entity
        ReviewToken reviewToken = new ReviewToken();
        reviewToken.setToken(token);
        reviewToken.setCustomerId(customerId);
        reviewToken.setCustomerEmail(customer.getEmail());
        reviewToken.setCustomerName(customer.getFirstName());
        reviewToken.setExpiresAt(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000));
        reviewToken.setCreatedAt(new Date());
        reviewToken.setIsUsed(false);

        // Save token to MongoDB
        reviewTokenService.saveToken(reviewToken);

        String reviewLink = "https://vute.in/review?token=" + token;

        emailService.sendReviewRequestEmail(customer.getEmail(), customer.getFirstName(), product.getTitle(), reviewLink);

        return ResponseEntity.ok("Review request email sent successfully.");
    }
}
