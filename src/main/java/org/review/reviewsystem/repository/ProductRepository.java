package org.review.reviewsystem.repository;

import org.review.reviewsystem.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
