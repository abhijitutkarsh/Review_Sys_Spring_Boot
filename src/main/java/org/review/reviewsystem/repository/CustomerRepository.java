package org.review.reviewsystem.repository;

import org.review.reviewsystem.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {
    Customer findByIdAndIsVerifiedTrue(String id);
}