package org.review.reviewsystem.repository;

import org.review.reviewsystem.model.ReviewToken;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewTokenRepository extends MongoRepository<ReviewToken, String> {
    // Add any custom queries if needed
}
