package org.review.reviewsystem.service;

import org.review.reviewsystem.model.ReviewToken;
import org.review.reviewsystem.repository.ReviewTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewTokenService {

    @Autowired
    private ReviewTokenRepository repository;

    public ReviewToken saveToken(ReviewToken token) {
        return repository.save(token);
    }

    // Add find methods if needed for validation
}
