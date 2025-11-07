package org.review.reviewsystem.repository;

import org.review.reviewsystem.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
}
